package pl.drone_zone;

import org.springframework.stereotype.Service;
import pl.drone_zone.api.OpenCategoryService;
import pl.drone_zone.api.models.Airspace;
import pl.drone_zone.api.models.SubCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OpenCategoryServiceImpl implements OpenCategoryService {

    private static final String COLOR_PROHIBITED = "#D9534F";
    private static final String COLOR_RESTRICTED = "#F0AD4E";
    private static final String COLOR_CAUTION = "#FFEE88";
    private static final String COLOR_ALLOWED = "#5CB85C";


    private static final Set<Integer> PROHIBITED_TYPES = Set.of(
            3,  // Prohibited
            9,  // TSA
            30, // MRT
            7,  // TMA
            31  // TFR
    );

    private static final Set<Integer> RESTRICTED_TYPES = Set.of(
            1,  // Restricted
            4,  // CTR
            13, // ATZ
            8,  // TRA
            19  // Protected Area
    );

    private static final Set<Integer> INFORMATIONAL_TYPES = Set.of(
            6,  // RMZ
            12, // ADIZ
            17, // Alert Area
            18, // Warning Area
            28  // Aerial Sporting Or Recreational Activity
    );

    @Override
    public List<Airspace> getOpenZonesBySubCategory(List<Airspace> airspaceList, SubCategory subCategory) {
        List<Airspace> processedAirspaceList = new ArrayList<>();

        for (Airspace airspace : airspaceList) {
            Airspace processedAirspace = processAirspace(airspace, subCategory);
            if (processedAirspace != null) {
                processedAirspaceList.add(processedAirspace);
            }
        }

        return processedAirspaceList;
    }

    private Airspace processAirspace(Airspace airspace, SubCategory subCategory) {
        int type = airspace.type();
        String color = determineColor(airspace);
        String processedName = processAirspaceName(airspace.name(), type);

        if (type == 4 || type == 13) {
            return processCtrAtzZone(airspace, subCategory);
        }

        if(Objects.isNull(processedName)) {
            return null;
        }

        return airspace.toBuilder()
                .name(processedName)
                .color(color)
                .build();
    }

    private String processAirspaceName(String name, int type) {
        if (PROHIBITED_TYPES.contains(type)) {
            return name + " (Strefa zakazana)";
        }

        if (RESTRICTED_TYPES.contains(type)) {
            return name + " (Wymagane zezwolenie)";
        }

        if (INFORMATIONAL_TYPES.contains(type)) {
            return name + " (Strefa informacyjna)";
        }

        return null;
    }

    private Airspace processCtrAtzZone(Airspace airspace, SubCategory subCategory) {
        String zoneName = airspace.name();
        String color;
        String description;

        switch (subCategory) {
            case A1:
                if (airspace.type() == 4) {
                    description = " (Do 30m AGL bez zgody)";
                    color = COLOR_ALLOWED;
                } else {
                    description = " (Do 30m AGL za zgodą)";
                    color = COLOR_RESTRICTED;
                }
                break;

            case A2:
                description = " (Do 100m AGL za zgodą)";
                color = COLOR_RESTRICTED;
                break;

            case A3:
                description = " (Wymagane zezwolenie ULC)";
                color = COLOR_RESTRICTED;
                break;

            default:
                return null;
        }

        return airspace.toBuilder()
                .name(zoneName + description)
                .color(color)
                .build();
    }

    private String determineColor(Airspace airspace) {
        if (PROHIBITED_TYPES.contains(airspace.type())) {
            return COLOR_PROHIBITED;
        }

        if (airspace.lowerLimit() != null) {
            if (airspace.lowerLimit().value() <= 120) {
                return RESTRICTED_TYPES.contains(airspace.type()) ? COLOR_RESTRICTED : COLOR_CAUTION;
            } else {
                return COLOR_ALLOWED;
            }
        }

        if (airspace.upperLimit() != null) {
            if (airspace.upperLimit().value() <= 120) {
                return COLOR_PROHIBITED;
            }
        }

        if (RESTRICTED_TYPES.contains(airspace.type())) {
            return COLOR_RESTRICTED;
        }
        if (INFORMATIONAL_TYPES.contains(airspace.type())) {
            return COLOR_CAUTION;
        }

        return COLOR_PROHIBITED;
    }
}

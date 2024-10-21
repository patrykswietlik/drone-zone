package pl.drone_zone;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.drone_zone.api.AirspaceService;
import pl.drone_zone.api.OpenCategoryService;
import pl.drone_zone.api.models.Airspace;
import pl.drone_zone.api.models.Geometry;
import pl.drone_zone.api.models.HeightLimit;
import pl.drone_zone.api.models.SubCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AirspaceServiceImpl implements AirspaceService {

    private final WebClient openAipClient;

    private final OpenCategoryService openCategoryService;

    @Autowired
    public AirspaceServiceImpl(
            @Qualifier("OPEN_AIP") WebClient openAipClient,
            OpenCategoryService openCategoryService
    ) {
        this.openAipClient = openAipClient;
        this.openCategoryService = openCategoryService;
    }

    @Override
    public List<Airspace> getAll() {
        JsonNode response = null;

        try {
            response = openAipClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/airspaces")
                            .queryParam("country", "PL")
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (Objects.isNull(response)) {
            throw new RuntimeException("Failed to fetch");
        }

        List<Airspace> airspaces = new ArrayList<>();
        response.get("items").forEach(item -> {
            Airspace airspace = toAirspace(item);
            airspaces.add(airspace);
        });

        return airspaces;
    }

    @Override
    public List<Airspace> getOpenZonesBySubCategory(SubCategory subCategory) {
        return openCategoryService.getOpenZonesBySubCategory(
                getAll(),
                subCategory
        );
    }

    private Airspace toAirspace(JsonNode node) {
        List<List<Coordinate>> coordinates = new ArrayList<>();
        node.get("geometry").get("coordinates").forEach(cNode -> {
            List<Coordinate> polygon = new ArrayList<>();
            cNode.forEach(coord -> {
                double lng = coord.get(0).asDouble();
                double lat = coord.get(1).asDouble();
                polygon.add(Coordinate.builder().lat(lat).lng(lng).build());
            });
            coordinates.add(polygon);
        });

        Geometry geometry = Geometry.builder()
                .type(node.get("geometry").get("type").asText())
                .coordinates(coordinates)
                .build();

        return Airspace.builder()
                .id(node.get("_id").asText())
                .name(node.get("name").asText())
                .type(node.get("type").asInt())
                .icaoClass(node.get("icaoClass").asInt())
                .onDemand(node.get("onDemand").asBoolean())
                .onRequest(node.get("onRequest").asBoolean())
                .byNotam(node.get("byNotam").asBoolean())
                .specialAgreement(node.get("specialAgreement").asBoolean())
                .requestCompliance(node.get("requestCompliance").asBoolean())
                .upperLimit(toHeightLimit(node.get("upperLimit")))
                .lowerLimit(toHeightLimit(node.get("lowerLimit")))
                .geometry(geometry)
                .build();
    }

    private HeightLimit toHeightLimit(JsonNode node) {
        int unit = node.get("unit").asInt();

        return HeightLimit.builder()
                .value(calculateUnits(node.get("value").asInt(), unit))
                .unit(unit)
                .build();
    }

    private double calculateUnits(int value, int type) {
        if (type == 0) {
            return value;
        }

        // flight level to meters conversion
        if (type == 6) {
            return switch (value) {
                case 10 -> 305;
                case 30 -> 914;
                case 35 -> 1067;
                case 50 -> 1524;
                case 55 -> 1676;
                case 70 -> 2133;
                case 75 -> 2286;
                case 90 -> 2743;
                case 95 -> 2895;
                case 110 -> 3353;
                case 115 -> 3505;
                case 130 -> 3962;
                case 135 -> 4115;
                case 150 -> 4572;
                case 155 -> 4724;
                case 170 -> 5181;
                case 175 -> 5334;
                case 190 -> 5791;
                case 195 -> 5943;
                case 210 -> 6400;
                case 230 -> 7010;
                case 250 -> 7620;
                case 270 -> 8229;
                case 290 -> 8839;
                case 330 -> 10058;
                case 370 -> 11277;
                case 410 -> 12496;
                case 450 -> 13715;
                case 490 -> 14934;
                default -> value * 30.5;
            };
        }

        // feet to meters conversion
        return value * 0.3048;
    }
}

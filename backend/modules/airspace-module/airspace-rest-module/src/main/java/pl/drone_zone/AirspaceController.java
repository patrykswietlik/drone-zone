package pl.drone_zone;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.drone_zone.api.AirspaceService;
import pl.drone_zone.api.models.Airspace;
import pl.drone_zone.api.models.AirspaceResponse;
import pl.drone_zone.api.models.AirspaceType;
import pl.drone_zone.api.models.SubCategory;

import java.util.List;

@RequestMapping("/airspaces")
@CrossOrigin
@RequiredArgsConstructor
@RestController
public class AirspaceController {

    private final AirspaceService airspaceService;

    @GetMapping
    public List<Airspace> getAllAirspaces() {
        return airspaceService.getAll();
    }

    @GetMapping("/open")
    public AirspaceResponse getOpenZones(@RequestParam SubCategory subCategory) {
        return AirspaceResponse.builder()
                .type(AirspaceType.OPEN)
                .airspaceList(airspaceService.getOpenZonesBySubCategory(subCategory))
                .build();
    }
}

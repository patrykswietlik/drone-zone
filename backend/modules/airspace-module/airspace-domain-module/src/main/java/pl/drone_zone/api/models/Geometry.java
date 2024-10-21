package pl.drone_zone.api.models;

import lombok.Builder;
import pl.drone_zone.Coordinate;

import java.util.List;

@Builder
public record Geometry(
        String type,
        List<List<Coordinate>> coordinates
) {
}

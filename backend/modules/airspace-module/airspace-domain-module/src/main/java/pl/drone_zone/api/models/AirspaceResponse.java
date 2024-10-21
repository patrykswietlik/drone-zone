package pl.drone_zone.api.models;

import lombok.Builder;

import java.util.List;

@Builder
public record AirspaceResponse(
        AirspaceType type,
        List<Airspace> airspaceList
) {
}

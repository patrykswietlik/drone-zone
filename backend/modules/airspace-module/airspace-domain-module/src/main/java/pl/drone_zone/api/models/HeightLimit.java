package pl.drone_zone.api.models;

import lombok.Builder;

@Builder
public record HeightLimit(
        double value,
        int unit
) {
}

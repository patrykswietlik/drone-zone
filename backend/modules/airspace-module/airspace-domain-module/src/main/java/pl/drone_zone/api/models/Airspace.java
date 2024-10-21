package pl.drone_zone.api.models;

import lombok.Builder;

@Builder(toBuilder = true)
public record Airspace(
        String id,
        String name,
        String color,
        int type,
        int icaoClass,
        boolean onDemand,
        boolean onRequest,
        boolean byNotam,
        boolean specialAgreement,
        boolean requestCompliance,
        HeightLimit upperLimit,
        HeightLimit lowerLimit,
        Geometry geometry
) {
}

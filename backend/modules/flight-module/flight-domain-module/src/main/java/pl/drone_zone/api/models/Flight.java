package pl.drone_zone.api.models;

import lombok.Builder;
import pl.drone_zone.Coordinate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Flight(
        UUID id,
        Coordinate coordinate,
        double AGL,
        ZonedDateTime startTime,
        int flightDurationInSeconds,
        boolean isActive
) {
}

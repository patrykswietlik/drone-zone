package pl.drone_zone;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Builder(toBuilder = true)
@Embeddable
public record Coordinate(
        double lat,
        double lng
) {
}

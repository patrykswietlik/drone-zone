package pl.drone_zone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightRepositoryJpa extends JpaRepository<FlightEntity, UUID> {
    List<FlightEntity> findAllByIsActiveTrue();
}

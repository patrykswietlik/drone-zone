package pl.drone_zone;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.drone_zone.api.models.Flight;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class FlightRepositoryImpl implements FlightRepository {

    private final FlightRepositoryJpa repositoryJpa;

    @Override
    public List<Flight> getActiveFlights() {
        return toDomainList(repositoryJpa.findAllByIsActiveTrue());
    }

    @Override
    public Flight save(Flight flight) {
        return toDomain(
                repositoryJpa.save(
                        toEntity(flight)
                )
        );
    }

    @Transactional
    @Override
    public List<Flight> update(List<Flight> flights) {
        return flights.stream()
                .map(this::save)
                .toList();
    }

    private List<Flight> toDomainList(List<FlightEntity> flights) {
        return flights.stream()
                .map(this::toDomain)
                .toList();
    }

    private Flight toDomain(FlightEntity flightEntity) {
        return Flight.builder()
                .id(flightEntity.getId())
                .coordinate(flightEntity.getCoordinate())
                .AGL(flightEntity.getAGL())
                .startTime(flightEntity.getStartTime())
                .flightDurationInSeconds(flightEntity.getFlightDurationInSeconds())
                .isActive(flightEntity.isActive())
                .build();
    }

    private FlightEntity toEntity(Flight flight) {
        return FlightEntity.builder()
                .id(flight.id())
                .coordinate(flight.coordinate())
                .AGL(flight.AGL())
                .startTime(flight.startTime())
                .flightDurationInSeconds(flight.flightDurationInSeconds())
                .isActive(flight.isActive())
                .build();
    }
}

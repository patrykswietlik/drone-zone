package pl.drone_zone;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.drone_zone.api.FlightService;
import pl.drone_zone.api.models.Flight;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public List<Flight> getActiveFlights() {
        validateFlights();
        return flightRepository.getActiveFlights();
    }

    @Override
    public Flight create(Flight flight) {
        return flightRepository.save(
                flight.toBuilder()
                        .startTime(ZonedDateTime.parse(flight.startTime().toString()))
                        .build()
        );
    }

    private void validateFlights() {
        List<Flight> flights = flightRepository.getActiveFlights();

        flightRepository.update(
                flights.stream()
                        .map(flight -> flight.toBuilder()
                                .isActive(checkIfFlightNotPassed(flight))
                                .build())
                        .toList()
        );
    }

    private boolean checkIfFlightNotPassed(Flight flight) {
        return ZonedDateTime.now(
                ZoneId.of("Europe/Warsaw")).isBefore(
                        flight.startTime()
                                .plusSeconds(
                                        flight.flightDurationInSeconds()
                                )
                );
    }
}

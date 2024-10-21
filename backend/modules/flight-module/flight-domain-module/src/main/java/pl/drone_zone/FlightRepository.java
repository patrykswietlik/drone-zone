package pl.drone_zone;

import pl.drone_zone.api.models.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> getActiveFlights();
    Flight save(Flight flight);
    List<Flight> update(List<Flight> flights);
}

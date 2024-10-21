package pl.drone_zone.api;

import pl.drone_zone.api.models.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> getActiveFlights();
    Flight create(Flight flight);
}

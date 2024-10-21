package pl.drone_zone;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pl.drone_zone.api.FlightService;
import pl.drone_zone.api.models.Flight;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class FlightController {

    private final FlightService flightService;

    @MessageMapping("/flights.active")
    @SendTo("/topic/public")
    public List<Flight> getActiveFlights() {
        return flightService.getActiveFlights();
    }

    @MessageMapping("/flights.report")
    @SendTo("/topic/public")
    public Flight report(Flight flight) {
        return flightService.create(flight);
    }
}

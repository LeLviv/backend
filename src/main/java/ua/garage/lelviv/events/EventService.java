package ua.garage.lelviv.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public EventResponse createEvent(@Valid EventRequest request) {
        Event event = Event.builder().build();
        eventRepository.save(event);
        return new EventResponse("ok");
    }
}

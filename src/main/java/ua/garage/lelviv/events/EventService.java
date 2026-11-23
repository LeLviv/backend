package ua.garage.lelviv.events;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ua.garage.lelviv.events.entities.EventRequest;
import ua.garage.lelviv.events.entities.EventResponse;
import ua.garage.lelviv.events.entities.Event;
import ua.garage.lelviv.events.entities.EventRepository;
import ua.garage.lelviv.events.entities.EventSpecification;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final String EVENTS_UPLOAD_FOLDER = "events";

    private final EventRepository eventRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public Page<EventResponse> getEvents(
            String title,
            LocalDateTime from,
            LocalDateTime to,
            boolean freeOnly,
            boolean upcomingOnly,
            Pageable pageable
    ) {
        Specification<Event> spec = EventSpecification.withFilters(title, from, to, freeOnly, upcomingOnly);
        return eventRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        return toResponse(findEventOrThrow(id));
    }

    @Transactional
    public EventResponse createEvent(EventRequest request, MultipartFile thumbnail) {
        validateDates(request);

        String thumbnailPath = null;
        if (thumbnail != null && !thumbnail.isEmpty()) {
            thumbnailPath = fileStorageService.saveSafeFile(thumbnail, EVENTS_UPLOAD_FOLDER);
        }

        Event event = Event.builder()
                .title(request.title())
                .host(request.host())
                .thumbnailPath(thumbnailPath)
                .description(request.description())
                .eventStart(request.eventStart())
                .eventEnd(request.eventEnd())
                .location(request.location())
                .price(request.price())
                .url(request.url())
                .build();

        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request, MultipartFile thumbnail) {
        validateDates(request);
        Event event = findEventOrThrow(id);

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String oldPath = event.getThumbnailPath();
            String newPath = fileStorageService.saveSafeFile(thumbnail, EVENTS_UPLOAD_FOLDER);
            event.setThumbnailPath(newPath);
            fileStorageService.deleteFile(oldPath);
        }

        event.setTitle(request.title());
        event.setHost(request.host());
        event.setDescription(request.description());
        event.setEventStart(request.eventStart());
        event.setEventEnd(request.eventEnd());
        event.setLocation(request.location());
        event.setPrice(request.price());
        event.setUrl(request.url());

        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = findEventOrThrow(id);
        String thumbnailPath = event.getThumbnailPath();
        eventRepository.delete(event);
        fileStorageService.deleteFile(thumbnailPath);
    }

    private Event findEventOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + id));
    }

    private void validateDates(EventRequest request) {
        if (request.eventEnd() != null && request.eventEnd().isBefore(request.eventStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event end time cannot be before start time");
        }
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getHost(),
                event.getThumbnailPath(),
                event.getDescription(),
                event.getCreatedAt(),
                event.getEventStart(),
                event.getEventEnd(),
                event.getLocation(),
                event.getPrice(),
                event.getUrl()
        );
    }
}
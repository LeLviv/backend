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
import ua.garage.lelviv.events.entities.*;
import ua.garage.lelviv.storage.FileUploadService;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final String EVENTS_UPLOAD_FOLDER = "events";

    private final EventRepository eventRepository;
    private final FileUploadService fileUploadService;

    @Transactional(readOnly = true)
    public Page<EventResponse> getEvents(
            String title,
            LocalDateTime from,
            LocalDateTime to,
            boolean freeOnly,
            boolean upcomingOnly,
            Pageable pageable) {
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
            thumbnailPath = fileUploadService.saveSafeFile(thumbnail, EVENTS_UPLOAD_FOLDER);
        }

        Event event = Event.builder()
                .title(request.getTitle())
                .host(request.getHost())
                .thumbnailPath(thumbnailPath)
                .description(request.getDescription())
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .location(request.getLocation())
                .price(request.getPrice())
                .url(request.getUrl())
                .build();

        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request, MultipartFile thumbnail) {
        validateDates(request);
        Event event = findEventOrThrow(id);

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String oldPath = event.getThumbnailPath();
            String newPath = fileUploadService.saveSafeFile(thumbnail, EVENTS_UPLOAD_FOLDER);
            event.setThumbnailPath(newPath);
            fileUploadService.deleteFile(oldPath);
        }

        event.setTitle(request.getTitle());
        event.setHost(request.getHost());
        event.setDescription(request.getDescription());
        event.setEventStart(request.getEventStart());
        event.setEventEnd(request.getEventEnd());
        event.setLocation(request.getLocation());
        event.setPrice(request.getPrice());
        event.setUrl(request.getUrl());

        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = findEventOrThrow(id);
        String thumbnailPath = event.getThumbnailPath();
        eventRepository.delete(event);
        fileUploadService.deleteFile(thumbnailPath);
    }

    private Event findEventOrThrow(Long id) {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + id));
    }

    private void validateDates(EventRequest request) {
        if (request.getEventEnd() != null && request.getEventEnd().isBefore(request.getEventStart())) {
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
                event.getUrl());
    }
}

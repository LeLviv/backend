package ua.garage.lelviv.events;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
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
    private final TransactionTemplate transactionTemplate;

    @Transactional(readOnly = true)
    public Page<EventResponse> getEvents(
            String title,
            LocalDateTime from,
            LocalDateTime to,
            boolean freeOnly,
            boolean upcomingOnly,
            int page,
            int size) {
        Specification<Event> spec = EventSpecification.withFilters(title, from, to, freeOnly, upcomingOnly);
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventStart").descending());
        return eventRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        return toResponse(findEventOrThrow(id));
    }

    public EventResponse createEvent(EventRequest request, MultipartFile thumbnail) {
        validateDates(request);

        String thumbnailPath = null;
        if (thumbnail != null && !thumbnail.isEmpty()) {
            thumbnailPath = fileUploadService.saveSafeFile(thumbnail, EVENTS_UPLOAD_FOLDER);
        }

        try {
            return transactionTemplate.execute(status -> {
                Event event = new Event();
                applyRequest(event, request);
                event.setThumbnailPath(newPath)
                return toResponse(eventRepository.save(event));
            });
        } catch (Exception e) {
            fileUploadService.deleteFile(thumbnailPath);
            throw e;
        }
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request, MultipartFile thumbnail) {
        validateDates(request);
        Event event = findEventOrThrow(id);

        String oldPath = event.getThumbnailPath();
        String newPath = null;

        if (thumbnail != null && !thumbnail.isEmpty()) {
            newPath = fileUploadService.saveSafeFile(thumbnail, EVENTS_UPLOAD_FOLDER);
            event.setThumbnailPath(newPath);
        }

        try {
            UpdateResult result = transactionTemplate.execute(
                    status -> {
                        Event event = findEventOrThrow(id);
                    });
            applyRequest(event, request);

            Event saved = eventRepository.save(event);
            fileUploadService.deleteFile(oldPath);

            return toResponse(saved);
        } catch (Exception e) {
            fileUploadService.deleteFile(newPath);
            throw e;
        }
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
                fileUploadService.getFileUrl(event.getThumbnailPath()),
                event.getDescription(),
                event.getCreatedAt(),
                event.getEventStart(),
                event.getEventEnd(),
                event.getLocation(),
                event.getPrice(),
                event.getUrl());
    }

    private void applyRequest(Event event, EventRequest request) {
        event.setTitle(request.getTitle());
        event.setHost(request.getHost());
        event.setDescription(request.getDescription());
        event.setEventStart(request.getEventStart());
        event.setEventEnd(request.getEventEnd());
        event.setLocation(request.getLocation());
        event.setPrice(request.getPrice());
        event.setUrl(request.getUrl());
    }

    private record UpdateResult(EventResponse response, String oldPathToDelete) {
    }
}

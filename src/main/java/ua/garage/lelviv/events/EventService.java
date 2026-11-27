package ua.garage.lelviv.events;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import ua.garage.lelviv.events.entities.*;
import ua.garage.lelviv.events.exception.BadEventRequestException;
import ua.garage.lelviv.events.exception.EventNotFoundException;
import ua.garage.lelviv.storage.FileUploadService;
import ua.garage.lelviv.storage.StorageProperties;

@Service
@RequiredArgsConstructor
public class EventService {

    private final StorageProperties properties;
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
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventStart").ascending().and(Sort.by("id").ascending()));
        return eventRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        return toResponse(findEventOrThrow(id));
    }

    public EventResponse createEvent(EventRequest request, MultipartFile thumbnail) {
        validateDates(request);
        final String thumbnailPath = uploadIfPresent(thumbnail);

        try {
            return transactionTemplate.execute(status -> {
                Event event = new Event();
                applyRequest(event, request);
                event.setThumbnailPath(thumbnailPath);
                return toResponse(eventRepository.save(event));
            });
        } catch (Exception e) {
            deleteIfPresent(thumbnailPath);
            throw e;
        }
    }

    public EventResponse updateEvent(Long id, EventRequest request, MultipartFile thumbnail) {
        validateDates(request);
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException(id);
        }

        String newPath = uploadIfPresent(thumbnail);
        UpdateResult result;
        try {
            result = transactionTemplate.execute(status -> {
                Event event = findEventOrThrow(id);
                String oldPath = event.getThumbnailPath();
                applyRequest(event, request);
                if (newPath != null) {
                    event.setThumbnailPath(newPath);
                }
                Event saved = eventRepository.save(event);
                return new UpdateResult(toResponse(saved), newPath != null ? oldPath : null);
            });
        } catch (Exception e) {
            deleteIfPresent(newPath);
            throw e;
        }
        deleteIfPresent(result.oldPathToDelete());
        return result.response();
    }

    public void deleteEvent(Long id) {
        String thumbnailPath = transactionTemplate.execute(status -> {
            Event event = findEventOrThrow(id);
            String path = event.getThumbnailPath();
            eventRepository.delete(event);
            return path;
        });
        deleteIfPresent(thumbnailPath);
    }

    private Event findEventOrThrow(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    }

    private void validateDates(EventRequest request) {
        if (request.getEventEnd() != null && request.getEventEnd().isBefore(request.getEventStart())) {
            throw new BadEventRequestException("Event end time cannot be before start time");
        }
    }

    private void deleteIfPresent(String path) {
        if (path != null && !path.isBlank()) {
            fileUploadService.deleteFile(path);
        }
    }

    private String uploadIfPresent(MultipartFile thumbnail) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            return null;
        }
        return fileUploadService.saveSafeFile(thumbnail, properties.folders().events());
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

    private record UpdateResult(EventResponse response, String oldPathToDelete) {}
}

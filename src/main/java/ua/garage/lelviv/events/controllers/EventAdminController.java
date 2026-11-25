package ua.garage.lelviv.events.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.garage.lelviv.events.entities.EventRequest;
import ua.garage.lelviv.events.entities.EventResponse;
import ua.garage.lelviv.events.EventService;

@RestController
@RequestMapping("/api/admin/events")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponse> createEvent(
            @RequestPart("data") @Valid EventRequest request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        EventResponse created = eventService.createEvent(request, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EventResponse updateEvent(
            @PathVariable Long id,
            @RequestPart("data") @Valid EventRequest request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        return eventService.updateEvent(id, request, thumbnail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
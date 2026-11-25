package ua.garage.lelviv.events.controllers;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.events.EventService;
import ua.garage.lelviv.events.entities.EventResponse;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public Page<EventResponse> getEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "false") boolean freeOnly,
            @RequestParam(defaultValue = "false") boolean upcomingOnly,
            @PageableDefault(size = 12, sort = "eventStart", direction = Sort.Direction.ASC) Pageable pageable) {
        return eventService.getEvents(title, from, to, freeOnly, upcomingOnly, pageable);
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }
}

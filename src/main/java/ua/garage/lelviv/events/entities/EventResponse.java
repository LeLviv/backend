package ua.garage.lelviv.events.entities;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String host,
        String thumbnailUrl,
        String description,
        LocalDateTime createdAt,
        LocalDateTime eventStart,
        LocalDateTime eventEnd,
        String location,
        Integer price,
        String url) {}

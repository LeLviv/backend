package ua.garage.lelviv.events.entities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EventRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 256)
    private String title;

    @NotBlank(message = "Host is required")
    @Size(max = 256)
    private String host;

    @Size(max = 4096)
    private String description;

    private MultipartFile thumbnail;

    @NotNull(message = "Event start time is required")
    private LocalDateTime eventStart;

    private LocalDateTime eventEnd;

    @NotBlank(message = "Location is required")
    @Size(max = 512)
    private String location;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price cannot be negative")
    private Integer price;

    @Size(max = 512)
    private String url;
}

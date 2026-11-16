package ua.garage.lelviv.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String host;

    private String description;
    private MultipartFile thumbnail;

    private String eventStart;
    private String eventEnd;

    @NotBlank
    private String location;

    @PositiveOrZero
    private Integer price;

    private String url;
}

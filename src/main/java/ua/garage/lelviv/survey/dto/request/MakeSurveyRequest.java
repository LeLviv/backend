package ua.garage.lelviv.survey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MakeSurveyRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String question;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    @NotNull
    @Size(min = 2, message = "В опитуванні повинно бути щонайменше 2 варіанти")
    private List<@NotBlank String> options;
}

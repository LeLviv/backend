package ua.garage.lelviv.survey.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    @NotNull
    Long choice;
}

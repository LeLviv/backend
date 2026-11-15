package ua.garage.lelviv.survey.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyOptionDto {
    private Long position;
    private String option;
    private Integer votesCount;
}

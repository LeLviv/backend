package ua.garage.lelviv.survey.dto.response;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyResponseDto {
    private Long id;
    private String title;
    private String question;
    private Instant startTime;
    private Instant endTime;
    private String surveyStatus;
    private List<SurveyOptionDto> options;
}

package ua.garage.lelviv.survey.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SurveyExpiredException extends RuntimeException {
    public SurveyExpiredException(String message) {
        super(message);
    }
}

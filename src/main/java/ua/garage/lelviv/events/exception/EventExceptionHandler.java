package ua.garage.lelviv.events.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.garage.lelviv.common.exception.ProblemDetailFactory;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ua.garage.lelviv.events.controllers")
@RequiredArgsConstructor
public class EventExceptionHandler {
    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(EventNotFoundException.class)
    public ProblemDetail handleEventNotFoundException(EventNotFoundException ex) {
        return problemDetailFactory.create(
                HttpStatus.NOT_FOUND, "Event Not Found", "/events-unavailable", "Queried event does not exist");
    }

    @ExceptionHandler(BadEventRequestException.class)
    public ProblemDetail handleBadEventRequestException(BadEventRequestException ex) {
        return problemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "Bad Event Request",
                "/events-bad-request",
                "Event with sent id does not exist");
    }
}

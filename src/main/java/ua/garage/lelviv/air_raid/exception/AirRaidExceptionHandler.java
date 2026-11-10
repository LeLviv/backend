package ua.garage.lelviv.air_raid.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.garage.lelviv.common.exception.ProblemDetailFactory;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ua.garage.lelviv.air_raid.controllers")
@RequiredArgsConstructor
public class AirRaidExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(AlertStatusUpdateException.class)
    public ProblemDetail handleAlertStatusUpdateException(AlertStatusUpdateException ex) {
        return problemDetailFactory.create(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Air Raid Service Unavailable",
                "/alerts-unavailable",
                "Air raid alert service is temporarily unavailable");
    }
}

package ua.garage.lelviv.weather.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.garage.lelviv.common.exception.ProblemDetailFactory;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ua.garage.lelviv.weather")
@RequiredArgsConstructor
public class WeatherExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(WeatherServiceUnavailableException.class)
    public ProblemDetail handleWeatherUnavailable(WeatherServiceUnavailableException ex) {
        return problemDetailFactory.create(
                HttpStatus.SERVICE_UNAVAILABLE, "Weather Service Unavailable", "/weather-unavailable", ex.getMessage());
    }

    @ExceptionHandler(RegionNotFoundException.class)
    public ProblemDetail handleRegionNotFound(RegionNotFoundException ex) {
        return problemDetailFactory.create(
                HttpStatus.NOT_FOUND, "Region Not Found", "/region-not-found", ex.getMessage());
    }
}

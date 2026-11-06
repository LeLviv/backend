package ua.garage.lelviv.weather.exception;

import java.net.URI;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.garage.lelviv.common.exception.ProblemDetailFactory;
import ua.garage.lelviv.weather.config.WeatherBaseProperties;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ua.garage.lelviv.weather")
@RequiredArgsConstructor
public class WeatherExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(WeatherServiceUnavailableException.class)
    public ProblemDetail handleWeatherUnavailable(WeatherServiceUnavailableException ex) {
        return problemDetailFactory.create(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Weather Service Unavailable",
                "/weather-unavailable",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RegionNotFoundException.class)
    public ProblemDetail handleRegionNotFound(RegionNotFoundException ex) {
        return problemDetailFactory.create(
                HttpStatus.NOT_FOUND,
                "Region Not Found",
                "/region-not-found",
                ex.getMessage()
        );
    }
}

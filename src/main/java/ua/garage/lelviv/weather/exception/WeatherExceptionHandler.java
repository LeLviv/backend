package ua.garage.lelviv.weather.exception;

import java.net.URI;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.garage.lelviv.weather.config.WeatherBaseProperties;

@RestControllerAdvice(basePackages = "ua.garage.lelviv.weather")
@RequiredArgsConstructor
public class WeatherExceptionHandler {
    private final WeatherBaseProperties weatherBaseProperties;

    @ExceptionHandler(WeatherServiceUnavailableException.class)
    public ProblemDetail handleWeatherUnavailable(WeatherServiceUnavailableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        problemDetail.setTitle("Weather Service Unavailable");
        problemDetail.setType(URI.create(weatherBaseProperties.baseErrorsUrl() + "/weather-unavailable"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(RegionNotFoundException.class)
    public ProblemDetail handleRegionNotFound(RegionNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Region Not Found");
        problemDetail.setType(URI.create(weatherBaseProperties.baseErrorsUrl() + "/region-not-found"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}

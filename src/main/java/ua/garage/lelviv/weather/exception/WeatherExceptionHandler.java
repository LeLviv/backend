package ua.garage.lelviv.weather.exception;

import java.net.URI;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ua.garage.lelviv.weather")
public class WeatherExceptionHandler {

    @ExceptionHandler(WeatherServiceUnavailableException.class)
    public ProblemDetail handleWeatherUnavailable(WeatherServiceUnavailableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        problemDetail.setTitle("Weather Service Unavailable");
        problemDetail.setType(URI.create("https://api.lelviv.ua/errors/weather-unavailable"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}

package ua.garage.lelviv.weather.exception;

public class WeatherServiceUnavailableException extends RuntimeException {
    public WeatherServiceUnavailableException(String message) {
        super(message);
    }
}

package ua.garage.lelviv.events.exception;

public class BadEventRequestException extends RuntimeException {
    public BadEventRequestException(String message) {
        super(message);
    }
}

package ua.garage.lelviv.events.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event " + id.toString() + " not found");
    }
}

package ua.garage.lelviv.events;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import ua.garage.lelviv.events.entities.Event;

public class EventSpecification {

    public static Specification<Event> withFilters(
            String title, LocalDateTime from, LocalDateTime to, boolean freeOnly, boolean upcomingOnly) {
        List<Specification<Event>> specs = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            String pattern = "%" + title.trim().toLowerCase() + "%";
            specs.add((root, query, cb) -> cb.like(cb.lower(root.get("title")), pattern));
        }

        if (from != null) {
            specs.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("eventStart"), from));
        }

        if (to != null) {
            specs.add((root, query, cb) -> cb.lessThanOrEqualTo(root.get("eventStart"), to));
        }

        if (freeOnly) {
            specs.add((root, query, cb) -> cb.equal(root.get("price"), 0));
        }

        if (upcomingOnly) {
            specs.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("eventStart"), LocalDateTime.now()));
        }

        return Specification.allOf(specs);
    }
}

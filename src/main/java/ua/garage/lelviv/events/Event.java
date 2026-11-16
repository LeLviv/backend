package ua.garage.lelviv.events;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@Entity
@Table(name = "events")
@Valid
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String host;

    private String thumbnailPath;
    private String description;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime eventStart;

    @NotNull
    private LocalDateTime eventEnd;

    @NotNull
    private String location;

    @PositiveOrZero
    private Integer price;

    @Size(max = 512)
    private String url;
}

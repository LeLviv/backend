package ua.garage.lelviv.complaints;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String author;

    @NonNull
    private String problem;

    @Column(name = "photo_url")
    private String photoUrl;

    private Double latitude;
    private Double longitude;
}

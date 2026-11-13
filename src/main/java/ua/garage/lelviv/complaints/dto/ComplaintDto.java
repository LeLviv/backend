package ua.garage.lelviv.complaints.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplaintDto {
    private Long id;
    private String author;
    private String problem;
    private String photoPath;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}

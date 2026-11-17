package ua.garage.lelviv.complaints.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintRequest {
    @NotBlank(message = "Complaint cannot be empty")
    private String complaint;

    @Min(value = -90, message = "Invalid latitude")
    @Max(value = 90, message = "Invalid latitude")
    private Double latitude;

    @Min(value = -180, message = "Invalid longitude")
    @Max(value = 180, message = "Invalid longitude")
    private Double longitude;

    private MultipartFile photo;
}

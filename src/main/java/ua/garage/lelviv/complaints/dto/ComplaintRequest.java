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
    @NotBlank(message = "Повинен бути текст скарги")
    private String complaint;

    @Min(value = -90, message = "Невірна широта")
    @Max(value = 90, message = "Невірна широта")
    private Double latitude;

    @Min(value = -180, message = "Невірна довгота")
    @Max(value = 180, message = "Невірна довгота")
    private Double longitude;

    private MultipartFile photo;
}

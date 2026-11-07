package ua.garage.lelviv.complaints;

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

    private Double latitude;
    private Double longitude;

    private MultipartFile photo;
}

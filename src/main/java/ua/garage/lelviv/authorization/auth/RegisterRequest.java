package ua.garage.lelviv.authorization.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 2, max = 64, message = "Ім'я повинне містити мінімум 2 літери")
    private String username;

    @NotBlank
    @Size(min = 12, max = 12, message = "Номер телефону повинен містити 12 символів")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Пароль повинен містити мінімум 8 літер")
    private String password;
}

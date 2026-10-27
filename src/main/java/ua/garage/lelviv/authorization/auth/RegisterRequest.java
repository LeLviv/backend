package ua.garage.lelviv.authorization.auth;

import jakarta.validation.constraints.Email;
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
    private String visibleName;

    @NotBlank
    @Email(message = "Введено некоректний E-mail")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Пароль повинен містити мінімум 8 літер")
    private String password;
}

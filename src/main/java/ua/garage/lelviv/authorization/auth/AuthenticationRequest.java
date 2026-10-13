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
public class AuthenticationRequest {

    @NotBlank(message = "Номер телефону обов'язковий")
    private String phoneNumber;

    @NotBlank(message = "Поле пароль не може бути порожнім")
    @Size(min = 8, message = "Пароль містить мінімум 8 символів")
    private String password;
}

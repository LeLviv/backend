package ua.garage.lelviv.air_raid.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.firebase")
public record FirebaseProperties(
        @NotNull(message = "Firebase credentials are not set")
        @NotEmpty(message = "Firebase credentials are empty")
        @Size(min = 20, message = "Firebase credentials are too short")
        String credentials,

        @NotNull Boolean enabled) {}

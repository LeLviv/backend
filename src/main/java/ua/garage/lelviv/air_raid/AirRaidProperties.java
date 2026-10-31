package ua.garage.lelviv.air_raid;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.air-raid")
public record AirRaidProperties(
        @URL(message = "Invalid URL") @NotBlank(message = "Base URL is required")
        String baseUrl,

        @NotBlank(message = "Secret key is required to access air raid API")
        String secretKey,

        @NotBlank(message = "Region ID is required") String regionId) {}

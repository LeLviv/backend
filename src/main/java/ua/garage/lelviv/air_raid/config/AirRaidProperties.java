package ua.garage.lelviv.air_raid.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.air-raid")
@ConditionalOnProperty(name = "app.air-raid.api-query-enabled", havingValue = "true")
public record AirRaidProperties(
        @URL(message = "Invalid URL") @NotBlank(message = "Base URL is required")
        String baseUrl,

        @NotBlank(message = "Secret key is required to access air raid API")
        String secretKey,

        @NotBlank(message = "Region ID is required") String regionId,

        @NotBlank(message = "Timezone is required") String timeZone,

        @NotNull @Min(value = 100) Integer connectionTimeout,

        @NotNull @Min(value = 100) Integer readTimeout,

        @NotNull boolean sendNotificationOnUpdate) {}

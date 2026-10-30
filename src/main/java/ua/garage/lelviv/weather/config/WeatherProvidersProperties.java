package ua.garage.lelviv.weather.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.weather")
public record WeatherProvidersProperties(@NotNull @Valid Map<String, ProviderConfig> provider) {

    public record ProviderConfig(
            boolean enabled,

            @NotBlank(message = "Name is required") String name,

            @NotBlank(message = "Base URL is required") @URL(message = "Base URL must be a valid URL")
            String baseUrl,

            @NotNull @DefaultValue("2s") Duration connectTimeout,

            @NotNull @DefaultValue("5s") Duration readTimeout) {}
}

package ua.garage.lelviv.weather.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.weather")
public record RegionMap(@NotEmpty Map<String, @Valid Coordinates> region) {}

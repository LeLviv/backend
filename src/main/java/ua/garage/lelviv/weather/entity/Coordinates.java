package ua.garage.lelviv.weather.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Coordinates(
        @NotNull @Min(-90) @Max(90) Double latitude,

        @NotNull @Min(-180) @Max(180) Double longitude) {}

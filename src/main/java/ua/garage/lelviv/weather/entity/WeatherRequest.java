package ua.garage.lelviv.weather.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WeatherRequest(
        @NotNull @Min(-90) @Max(90) Double latitude,

        @NotNull @Min(-180) @Max(180) Double longitude) {

    public WeatherRequest() {
        this(49.8383, 24.0232);
    }
}

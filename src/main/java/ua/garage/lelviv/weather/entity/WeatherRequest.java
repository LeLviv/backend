package ua.garage.lelviv.weather.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WeatherRequest(@NotNull @NotBlank String region) {}

package ua.garage.lelviv.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.With;

@Builder
public record WeatherDailyDto(
        @JsonProperty("current_temperature_celsius") Double currentTempCelsius,

        @JsonProperty("apparent_temperature_celsius") Double apparentTempCelsius,

        @JsonProperty("max_temperature_celsius") Double maxTempCelsius,

        @JsonProperty("min_temperature_celsius") Double minTempCelsius,

        @JsonProperty("weather_code") Short weatherCode,

        @JsonProperty("is_stale") @With Boolean isStale) {}

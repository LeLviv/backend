package ua.garage.lelviv.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.With;

@Builder
public record WeatherWeeklyDto(
        List<Report> reports,

        @JsonProperty("is_stale") @With Boolean isStale) {

    @Builder
    public record Report(
            @JsonProperty("time") LocalDate time,

            @JsonProperty("max_temperature_celsius") Double maxTempCelsius,

            @JsonProperty("min_temperature_celsius") Double minTempCelsius,

            @JsonProperty("max_precipitation_probability_percent")
            Short maxPrecipitationProbPercent,

            @JsonProperty("weather_code") Short weatherCode) {}
}

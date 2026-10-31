package ua.garage.lelviv.weather.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.With;

@Builder
public record WeatherHourlyDto(
        List<Report> reports,

        @JsonProperty("is_stale") @With Boolean isStale) {

    @Builder
    public record Report(
            @JsonFormat(pattern = "HH:mm") LocalTime time,

            @JsonProperty("temperature_celsius") Double tempCelsius,

            @JsonProperty("apparent_temperature_celsius") Double apparentTempCelsius,

            @JsonProperty("precipitation_probability_percent")
            Short precipitationProbPercent,

            @JsonProperty("weather_code") Short weatherCode) {}
}

package ua.garage.lelviv.weather.provider.openMeteo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoHourlyResponse(Hourly hourly) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Hourly(
            @JsonProperty("time") List<LocalTime> times,

            @JsonProperty("temperature_2m") List<Double> tempCelsius,

            @JsonProperty("apparent_temperature") List<Double> apparentTempCelsius,

            @JsonProperty("precipitation_probability") List<Short> precipitationProbPercent,

            @JsonProperty("weather_code") List<Short> descriptionCodes) {}
}

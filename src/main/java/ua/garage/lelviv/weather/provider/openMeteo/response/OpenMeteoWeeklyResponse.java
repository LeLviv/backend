package ua.garage.lelviv.weather.provider.openMeteo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoWeeklyResponse(@JsonProperty("daily") Weekly weekly) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Weekly(
            @JsonProperty("time") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            List<LocalDate> times,

            @JsonProperty("temperature_2m_max") List<Double> maxTempCelsius,

            @JsonProperty("temperature_2m_min") List<Double> minTempCelsius,

            @JsonProperty("precipitation_probability_max") List<Short> maxPrecipitationProbPercent,

            @JsonProperty("weather_code") List<Short> descriptionCodes) {}
}

package ua.garage.lelviv.weather.provider.openMeteo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoDailyResponse(Current current, Daily daily) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Current(
            @JsonProperty("temperature_2m") Double currentTempCelsius,

            @JsonProperty("apparent_temperature") Double apparentTempCelsius,

            @JsonProperty("weather_code") Short descriptionCode) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Daily(
            @JsonProperty("temperature_2m_max") List<Double> maxTempCelsius,

            @JsonProperty("temperature_2m_min") List<Double> minTempCelsius) {}
}

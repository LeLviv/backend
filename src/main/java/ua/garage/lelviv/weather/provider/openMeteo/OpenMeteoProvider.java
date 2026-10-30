package ua.garage.lelviv.weather.provider.openMeteo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.weather.entity.*;
import ua.garage.lelviv.weather.provider.WeatherProvider;
import ua.garage.lelviv.weather.provider.openMeteo.response.OpenMeteoDailyResponse;
import ua.garage.lelviv.weather.provider.openMeteo.response.OpenMeteoHourlyResponse;
import ua.garage.lelviv.weather.provider.openMeteo.response.OpenMeteoWeeklyResponse;

@RequiredArgsConstructor
public class OpenMeteoProvider implements WeatherProvider {

    @Qualifier("openMeteoRestClient")
    private final RestClient openMeteoRestClient;
    private final OpenMeteoMapper openMeteoMapper;

    @Override
    @Retry(name = "open-meteo")
    @CircuitBreaker(name = "open-meteo")
    public WeatherHourlyDto fetchWeatherHourly(Double latitude, Double longitude) {
        OpenMeteoHourlyResponse response = openMeteoRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam(
                                "hourly", "temperature_2m,apparent_temperature,precipitation_probability,weather_code")
                        .queryParam("forecast_days", 2)
                        .queryParam("timezone", "Europe/Kyiv")
                        .build())
                .retrieve()
                .body(OpenMeteoHourlyResponse.class);

        return openMeteoMapper.toWeatherHourlyDto(response);
    }

    @Override
    @Retry(name = "open-meteo")
    @CircuitBreaker(name = "open-meteo")
    public WeatherWeeklyDto fetchWeatherWeekly(Double latitude, Double longitude) {
        OpenMeteoWeeklyResponse response = openMeteoRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam(
                                "daily",
                                "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max")
                        .queryParam("timezone", "Europe/Kyiv")
                        .build())
                .retrieve()
                .body(OpenMeteoWeeklyResponse.class);

        return openMeteoMapper.toWeatherWeeklyDto(response);
    }

    @Override
    @Retry(name = "open-meteo")
    @CircuitBreaker(name = "open-meteo")
    public WeatherDailyDto fetchWeatherDaily(Double latitude, Double longitude) {
        OpenMeteoDailyResponse response = openMeteoRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("daily", "temperature_2m_max,temperature_2m_min")
                        .queryParam("current", "weather_code,temperature_2m,apparent_temperature")
                        .queryParam("forecast_days", 1)
                        .queryParam("timezone", "Europe/Kyiv")
                        .build())
                .retrieve()
                .body(OpenMeteoDailyResponse.class);

        return openMeteoMapper.toWeatherDailyDto(response);
    }
}

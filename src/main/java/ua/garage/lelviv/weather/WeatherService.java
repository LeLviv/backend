package ua.garage.lelviv.weather;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.weather.entity.WeatherDailyDto;
import ua.garage.lelviv.weather.entity.WeatherHourlyDto;
import ua.garage.lelviv.weather.entity.WeatherRequest;
import ua.garage.lelviv.weather.entity.WeatherWeeklyDto;
import ua.garage.lelviv.weather.exception.WeatherServiceUnavailableException;
import ua.garage.lelviv.weather.provider.WeatherProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherProvider weatherProvider;
    private final WeatherFallbackStore weatherFallbackStore;

    @Cacheable(
            value = "#{@weatherCacheProperties.hourly().name()}",
            key = "'hourly_' + #request.latitude + '_' + #request.longitude")
    @CircuitBreaker(name = "weather-service", fallbackMethod = "getHourlyFallback")
    public WeatherHourlyDto getWeatherHourly(WeatherRequest request) {
        WeatherHourlyDto dto = weatherProvider.fetchWeatherHourly(request.getLatitude(), request.getLongitude());
        weatherFallbackStore.save("hourly", request.getLatitude(), request.getLongitude(), dto);
        return dto;
    }

    public WeatherHourlyDto getHourlyFallback(WeatherRequest request, Throwable throwable) {
        log.warn(
                "Hourly weather failed ({}). Fetching fallback for ({}, {})",
                throwable.getMessage(),
                request.getLatitude(),
                request.getLongitude());
        return weatherFallbackStore
                .get("hourly", request.getLatitude(), request.getLongitude(), WeatherHourlyDto.class)
                .map(stale -> stale.withIsStale(true))
                .orElseThrow(() -> new WeatherServiceUnavailableException(
                        "Погодинний прогноз погоди тимчасово недоступний для координат: " + request.getLatitude() + ", "
                                + request.getLongitude()));
    }

    @Cacheable(
            value = "#{@weatherCacheProperties.weekly().name()}",
            key = "'weekly_' + #request.latitude + '_' + #request.longitude")
    @CircuitBreaker(name = "weather-service", fallbackMethod = "getWeeklyFallback")
    public WeatherWeeklyDto getWeatherWeekly(WeatherRequest request) {
        WeatherWeeklyDto dto = weatherProvider.fetchWeatherWeekly(request.getLatitude(), request.getLongitude());
        weatherFallbackStore.save("weekly", request.getLatitude(), request.getLongitude(), dto);
        return dto;
    }

    public WeatherWeeklyDto getWeeklyFallback(WeatherRequest request, Throwable throwable) {
        log.warn(
                "Weekly weather failed ({}). Fetching fallback for ({}, {})",
                throwable.getMessage(),
                request.getLatitude(),
                request.getLongitude());
        return weatherFallbackStore
                .get("weekly", request.getLatitude(), request.getLongitude(), WeatherWeeklyDto.class)
                .map(stale -> stale.withIsStale(true))
                .orElseThrow(() -> new WeatherServiceUnavailableException(
                        "Тижневий прогноз погоди тимчасово недоступний для координат: " + request.getLatitude() + ", "
                                + request.getLongitude()));
    }

    @Cacheable(
            value = "#{@weatherCacheProperties.daily().name()}",
            key = "'daily_' + #request.latitude + '_' + #request.longitude")
    @CircuitBreaker(name = "weather-service", fallbackMethod = "getDailyFallback")
    public WeatherDailyDto getWeatherDaily(WeatherRequest request) {
        WeatherDailyDto dto = weatherProvider.fetchWeatherDaily(request.getLatitude(), request.getLongitude());
        weatherFallbackStore.save("daily", request.getLatitude(), request.getLongitude(), dto);
        return dto;
    }

    public WeatherDailyDto getDailyFallback(WeatherRequest request, Throwable throwable) {
        log.warn(
                "Daily weather failed ({}). Fetching fallback for ({}, {})",
                throwable.getMessage(),
                request.getLatitude(),
                request.getLongitude());
        return weatherFallbackStore
                .get("daily", request.getLatitude(), request.getLongitude(), WeatherDailyDto.class)
                .map(stale -> stale.withIsStale(true))
                .orElseThrow(() ->
                        new WeatherServiceUnavailableException("Прогноз погоди тимчасово недоступний для координат: "
                                + request.getLatitude() + ", " + request.getLongitude()));
    }
}

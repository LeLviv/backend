package ua.garage.lelviv.weather;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.weather.entity.*;
import ua.garage.lelviv.weather.exception.WeatherServiceUnavailableException;
import ua.garage.lelviv.weather.provider.WeatherProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherClientService {
    private final WeatherProvider weatherProvider;
    private final WeatherFallbackStore weatherFallbackStore;

    @Cacheable(value = "#{@weatherCacheProperties.hourly().name()}", key = "'hourly_' + #region")
    @CircuitBreaker(name = "weather-client-service", fallbackMethod = "getWeatherHourlyProtectedFallback")
    public WeatherHourlyDto getWeatherHourlyProtected(Coordinates coordinates, String region) {
        WeatherHourlyDto dto = weatherProvider.fetchWeatherHourly(coordinates.latitude(), coordinates.longitude());
        weatherFallbackStore.save("hourly", region, dto);
        return dto;
    }

    public WeatherHourlyDto getWeatherHourlyProtectedFallback(
            Coordinates coordinates, String region, Throwable throwable) {
        log.warn("Hourly weather failed ({}). Fetching fallback for ({})", throwable.getMessage(), region);
        return weatherFallbackStore
                .get("hourly", region, WeatherHourlyDto.class)
                .map(stale -> stale.withIsStale(true))
                .orElseThrow(() -> new WeatherServiceUnavailableException(
                        "Погодинний прогноз погоди тимчасово недоступний для регіону: " + region));
    }

    @Cacheable(value = "#{@weatherCacheProperties.weekly().name()}", key = "'weekly_' + #region")
    @CircuitBreaker(name = "weather-client-service", fallbackMethod = "getWeatherWeeklyProtectedFallback")
    public WeatherWeeklyDto getWeatherWeeklyProtected(Coordinates coordinates, String region) {
        WeatherWeeklyDto dto = weatherProvider.fetchWeatherWeekly(coordinates.latitude(), coordinates.longitude());
        weatherFallbackStore.save("weekly", region, dto);
        return dto;
    }

    public WeatherWeeklyDto getWeatherWeeklyProtectedFallback(
            Coordinates coordinates, String region, Throwable throwable) {
        log.warn("Weekly weather failed ({}). Fetching fallback for ({})", throwable.getMessage(), region);
        return weatherFallbackStore
                .get("weekly", region, WeatherWeeklyDto.class)
                .map(stale -> stale.withIsStale(true))
                .orElseThrow(() -> new WeatherServiceUnavailableException(
                        "Тижневий прогноз погоди тимчасово недоступний для регіону: " + region));
    }

    @Cacheable(value = "#{@weatherCacheProperties.daily().name()}", key = "'daily_' + #region")
    @CircuitBreaker(name = "weather-client-service", fallbackMethod = "getWeatherDailyProtectedFallback")
    public WeatherDailyDto getWeatherDailyProtected(Coordinates coordinates, String region) {
        WeatherDailyDto dto = weatherProvider.fetchWeatherDaily(coordinates.latitude(), coordinates.longitude());
        weatherFallbackStore.save("daily", region, dto);
        return dto;
    }

    public WeatherDailyDto getWeatherDailyProtectedFallback(
            Coordinates coordinates, String region, Throwable throwable) {
        log.warn("Daily weather failed ({}). Fetching fallback for ({})", throwable.getMessage(), region);
        return weatherFallbackStore
                .get("daily", region, WeatherDailyDto.class)
                .map(stale -> stale.withIsStale(true))
                .orElseThrow(() -> new WeatherServiceUnavailableException(
                        "Прогноз погоди тимчасово недоступний для регіону: " + region));
    }
}

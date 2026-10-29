package ua.garage.lelviv.weather;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import ua.garage.lelviv.weather.config.WeatherCacheProperties;

@Component
@RequiredArgsConstructor
public class WeatherFallbackStore {

    private final WeatherCacheProperties weatherCacheProperties;
    private final CacheManager weatherCacheManager;

    public void save(String type, Double lat, Double lon, Object payload) {
        Cache cache =
                weatherCacheManager.getCache(weatherCacheProperties.fallback().name());
        if (cache != null) {
            String key = buildKey(type, lat, lon);
            cache.put(key, payload);
        }
    }

    public <T> Optional<T> get(String type, Double lat, Double lon, Class<T> cls) {
        Cache cache =
                weatherCacheManager.getCache(weatherCacheProperties.fallback().name());
        if (cache == null) {
            return Optional.empty();
        }
        String key = buildKey(type, lat, lon);
        return Optional.ofNullable(cache.get(key, cls));
    }

    private String buildKey(String type, Double lat, Double lon) {
        return type + "_" + lat + "_" + lon;
    }
}

package ua.garage.lelviv.weather.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableConfigurationProperties(WeatherCacheProperties.class)
public class WeatherCacheConfig {

    @Bean
    public CacheManager weatherCacheManager(WeatherCacheProperties properties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.registerCustomCache(properties.daily().name(), buildCaffeine(properties.daily()));
        cacheManager.registerCustomCache(properties.hourly().name(), buildCaffeine(properties.hourly()));
        cacheManager.registerCustomCache(properties.weekly().name(), buildCaffeine(properties.weekly()));
        cacheManager.registerCustomCache(properties.fallback().name(), buildCaffeine(properties.fallback()));

        return cacheManager;
    }

    private Cache<Object, Object> buildCaffeine(WeatherCacheProperties.CacheProp prop) {
        return Caffeine.newBuilder()
                .expireAfterWrite(prop.ttl())
                .maximumSize(prop.maxSize())
                .build();
    }
}

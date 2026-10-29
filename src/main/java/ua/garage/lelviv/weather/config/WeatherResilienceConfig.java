package ua.garage.lelviv.weather.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WeatherResilienceProperties.class)
public class WeatherResilienceConfig {

    @Bean
    public CircuitBreakerRegistry weatherCircuitBreakerRegistry(WeatherResilienceProperties properties) {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();

        properties.circuitBreaker().forEach((name, prop) -> {
            var config = CircuitBreakerConfig.custom()
                    .slidingWindowSize(prop.slidingWindowSize())
                    .failureRateThreshold(prop.failureRateThreshold())
                    .waitDurationInOpenState(prop.waitDurationInOpenState())
                    .permittedNumberOfCallsInHalfOpenState(prop.permittedCallsInHalfOpen())
                    .automaticTransitionFromOpenToHalfOpenEnabled(true)
                    .build();

            registry.circuitBreaker(name, config);
        });

        return registry;
    }

    @Bean
    public RetryRegistry weatherRetryRegistry(WeatherResilienceProperties properties) {
        RetryRegistry registry = RetryRegistry.ofDefaults();

        properties.retry().forEach((name, prop) -> {
            var config = RetryConfig.custom()
                    .maxAttempts(prop.maxAttempts())
                    .waitDuration(prop.waitDuration())
                    .intervalFunction(IntervalFunction.ofExponentialBackoff(
                            prop.waitDuration(), prop.exponentialBackoffMultiplier()))
                    .build();

            registry.retry(name, config);
        });

        return registry;
    }
}

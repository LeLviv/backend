package ua.garage.lelviv.weather.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.application.weather.resilience")
public record WeatherResilienceProperties(
        @NotNull @Valid Map<String, CircuitBreakerProp> circuitBreaker,

        @NotNull @Valid Map<String, RetryProp> retry) {

    public record CircuitBreakerProp(
            @Positive @DefaultValue("10") Integer slidingWindowSize,

            @Positive @DefaultValue("50") Float failureRateThreshold,

            @DefaultValue("10s") Duration waitDurationInOpenState,

            @Positive @DefaultValue("3") Integer permittedCallsInHalfOpen,

            @DefaultValue("true") Boolean automaticTransitionFromOpenToHalfOpenEnabled) {}

    public record RetryProp(
            @Positive @DefaultValue("3") Integer maxAttempts,

            @DefaultValue("300ms") Duration waitDuration,

            @Positive @DefaultValue("2.0") Double exponentialBackoffMultiplier) {}
}

package ua.garage.lelviv.weather.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.weather.resilience")
public record WeatherResilienceProperties(
        @NotEmpty Map<String, @Valid CircuitBreakerProp> circuitBreaker,

        @NotEmpty Map<String, @Valid RetryProp> retry) {

    @Validated
    public record CircuitBreakerProp(
            @Positive @DefaultValue("10") Integer slidingWindowSize,

            @Positive @DefaultValue("50") Float failureRateThreshold,

            @DefaultValue("10s") Duration waitDurationInOpenState,

            @Positive @DefaultValue("3") Integer permittedCallsInHalfOpen,

            @DefaultValue("true") Boolean automaticTransitionFromOpenToHalfOpenEnabled) {}

    @Validated
    public record RetryProp(
            @Positive @DefaultValue("3") Integer maxAttempts,

            @DefaultValue("300ms") Duration waitDuration,

            @Positive @DefaultValue("2.0") Double exponentialBackoffMultiplier) {}
}

package ua.garage.lelviv.weather.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ua.garage.lelviv.weather.entity.WeatherDailyDto;
import ua.garage.lelviv.weather.entity.WeatherHourlyDto;
import ua.garage.lelviv.weather.entity.WeatherWeeklyDto;
import ua.garage.lelviv.weather.exception.WeatherProviderException;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class ChainWeatherProvider implements WeatherProvider {

    private final List<WeatherProvider> providers;

    @Override
    public WeatherHourlyDto fetchWeatherHourly(Double latitude, Double longitude) {
        for (WeatherProvider provider : providers) {
            if (provider == this) {
                continue;
            }

            try {
                log.info(
                        "Executing hourly weather request via [{}]",
                        provider.getClass().getSimpleName());
                return provider.fetchWeatherHourly(latitude, longitude);
            } catch (Exception e) {
                log.warn(
                        "Hourly provider [{}] failed. Cause: {}",
                        provider.getClass().getSimpleName(),
                        e.getMessage());
            }
        }

        log.error("All available hourly weather providers failed.");
        throw new WeatherProviderException("All available hourly weather providers failed");
    }

    @Override
    public WeatherWeeklyDto fetchWeatherWeekly(Double latitude, Double longitude) {
        for (WeatherProvider provider : providers) {
            if (provider == this) {
                continue;
            }

            try {
                log.info(
                        "Executing weekly weather request via [{}]",
                        provider.getClass().getSimpleName());
                return provider.fetchWeatherWeekly(latitude, longitude);
            } catch (Exception e) {
                log.warn(
                        "Weekly provider [{}] failed. Cause: {}",
                        provider.getClass().getSimpleName(),
                        e.getMessage());
            }
        }

        log.error("All available weekly weather providers failed.");
        throw new WeatherProviderException("All available weekly weather providers failed");
    }

    @Override
    public WeatherDailyDto fetchWeatherDaily(Double latitude, Double longitude) {
        for (WeatherProvider provider : providers) {
            if (provider == this) {
                continue;
            }

            try {
                log.info(
                        "Executing daily weather request via [{}]",
                        provider.getClass().getSimpleName());
                return provider.fetchWeatherDaily(latitude, longitude);
            } catch (Exception e) {
                log.warn(
                        "Daily provider [{}] failed. Cause: {}",
                        provider.getClass().getSimpleName(),
                        e.getMessage());
            }
        }

        log.error("All available daily weather providers failed.");
        throw new WeatherProviderException("All available daily weather providers failed");
    }
}

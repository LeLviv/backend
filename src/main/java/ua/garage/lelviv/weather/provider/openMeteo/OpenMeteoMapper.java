package ua.garage.lelviv.weather.provider.openMeteo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;
import ua.garage.lelviv.weather.entity.*;
import ua.garage.lelviv.weather.provider.openMeteo.response.OpenMeteoDailyResponse;
import ua.garage.lelviv.weather.provider.openMeteo.response.OpenMeteoHourlyResponse;
import ua.garage.lelviv.weather.provider.openMeteo.response.OpenMeteoWeeklyResponse;

@Component
public class OpenMeteoMapper {

    public WeatherHourlyDto toWeatherHourlyDto(OpenMeteoHourlyResponse response) {
        if (response == null || response.hourly() == null) {
            return WeatherHourlyDto.builder()
                    .reports(Collections.emptyList())
                    .isStale(true)
                    .build();
        }

        var hourly = response.hourly();
        List<LocalTime> times = hourly.times();
        List<Double> tempCelsius = hourly.tempCelsius();
        List<Double> apparentTempCelsius = hourly.apparentTempCelsius();
        List<Short> precipitationProbPercent = hourly.precipitationProbPercent();
        List<Short> weatherCodes = hourly.descriptionCodes();

        int minSize = IntStream.of(
                        times.size(),
                        tempCelsius.size(),
                        apparentTempCelsius.size(),
                        precipitationProbPercent.size(),
                        weatherCodes.size())
                .min()
                .orElse(0);

        LocalTime currentHour = LocalTime.now().truncatedTo(ChronoUnit.HOURS);
        int startIndex = IntStream.range(0, minSize)
                .filter(i -> !times.get(i).isBefore(currentHour))
                .findFirst()
                .orElse(0);

        int endIndex = Math.min(startIndex + 24, minSize);

        List<WeatherHourlyDto.Report> reports = IntStream.range(startIndex, endIndex)
                .mapToObj(i -> WeatherHourlyDto.Report.builder()
                        .time(times.get(i))
                        .tempCelsius(tempCelsius.get(i))
                        .apparentTempCelsius(apparentTempCelsius.get(i))
                        .precipitationProbPercent(precipitationProbPercent.get(i))
                        .weatherCode(weatherCodes.get(i))
                        .build())
                .toList();

        return WeatherHourlyDto.builder().reports(reports).isStale(false).build();
    }

    public WeatherWeeklyDto toWeatherWeeklyDto(OpenMeteoWeeklyResponse response) {
        if (response == null || response.weekly() == null) {
            return WeatherWeeklyDto.builder()
                    .reports(Collections.emptyList())
                    .isStale(true)
                    .build();
        }

        var weekly = response.weekly();
        List<LocalDate> times = weekly.times();
        List<Double> maxTempCelsius = weekly.maxTempCelsius();
        List<Double> minTempCelsius = weekly.minTempCelsius();
        List<Short> maxPrecipitationProbPercent = weekly.maxPrecipitationProbPercent();
        List<Short> weatherCodes = weekly.descriptionCodes();

        int minSize = IntStream.of(
                        times.size(),
                        maxTempCelsius.size(),
                        minTempCelsius.size(),
                        maxPrecipitationProbPercent.size(),
                        weatherCodes.size())
                .min()
                .orElse(0);

        List<WeatherWeeklyDto.Report> reports = IntStream.range(0, minSize)
                .mapToObj(i -> WeatherWeeklyDto.Report.builder()
                        .time(times.get(i))
                        .maxTempCelsius(maxTempCelsius.get(i))
                        .minTempCelsius(minTempCelsius.get(i))
                        .maxPrecipitationProbPercent(maxPrecipitationProbPercent.get(i))
                        .weatherCode(weatherCodes.get(i))
                        .build())
                .toList();

        return WeatherWeeklyDto.builder().reports(reports).isStale(false).build();
    }

    public WeatherDailyDto toWeatherDailyDto(OpenMeteoDailyResponse response) {
        if (response == null || response.current() == null || response.daily() == null) {
            return WeatherDailyDto.builder().isStale(true).build();
        }

        var current = response.current();
        var daily = response.daily();
        return WeatherDailyDto.builder()
                .currentTempCelsius(current.currentTempCelsius())
                .apparentTempCelsius(current.apparentTempCelsius())
                .maxTempCelsius(daily.maxTempCelsius().getFirst())
                .minTempCelsius(daily.minTempCelsius().getFirst())
                .weatherCode(current.descriptionCode())
                .isStale(false)
                .build();
    }
}

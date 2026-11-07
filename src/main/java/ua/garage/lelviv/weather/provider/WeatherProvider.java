package ua.garage.lelviv.weather.provider;

import ua.garage.lelviv.weather.entity.WeatherDailyDto;
import ua.garage.lelviv.weather.entity.WeatherHourlyDto;
import ua.garage.lelviv.weather.entity.WeatherWeeklyDto;

public interface WeatherProvider {
    WeatherHourlyDto fetchWeatherHourly(Double latitude, Double longitude);

    WeatherWeeklyDto fetchWeatherWeekly(Double latitude, Double longitude);

    WeatherDailyDto fetchWeatherDaily(Double latitude, Double longitude);
}

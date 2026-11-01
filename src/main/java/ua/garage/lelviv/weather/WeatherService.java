package ua.garage.lelviv.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.weather.entity.*;
import ua.garage.lelviv.weather.exception.RegionNotFoundException;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClientService weatherClientService;
    private final RegionMap regionMap;

    public WeatherHourlyDto getWeatherHourly(String region) {
        region = region.toLowerCase();
        Coordinates coordinates = regionMap.region().get(region);
        if (coordinates == null) {
            throw new RegionNotFoundException("Не було знайдено регіону: " + region);
        }

        return weatherClientService.getWeatherHourlyProtected(coordinates, region);
    }

    public WeatherWeeklyDto getWeatherWeekly(String region) {
        region = region.toLowerCase();
        Coordinates coordinates = regionMap.region().get(region);
        if (coordinates == null) {
            throw new RegionNotFoundException("Не було знайдено регіону: " + region);
        }

        return weatherClientService.getWeatherWeeklyProtected(coordinates, region);
    }

    public WeatherDailyDto getWeatherDaily(String region) {
        region = region.toLowerCase();
        Coordinates coordinates = regionMap.region().get(region);
        if (coordinates == null) {
            throw new RegionNotFoundException("Не було знайдено регіону: " + region);
        }

        return weatherClientService.getWeatherDailyProtected(coordinates, region);
    }
}

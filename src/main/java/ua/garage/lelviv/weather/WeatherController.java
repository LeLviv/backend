package ua.garage.lelviv.weather;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.garage.lelviv.weather.entity.WeatherDailyDto;
import ua.garage.lelviv.weather.entity.WeatherHourlyDto;
import ua.garage.lelviv.weather.entity.WeatherWeeklyDto;

@Validated
@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService service;

    @GetMapping("/hourly")
    public ResponseEntity<WeatherHourlyDto> getWeatherHourly(
            @RequestParam @NotBlank(message = "Регіон обов'язковий") String region) {
        return ResponseEntity.ok(service.getWeatherHourly(region));
    }

    @GetMapping("/weekly")
    public ResponseEntity<WeatherWeeklyDto> getWeatherWeekly(
            @RequestParam @NotBlank(message = "Регіон обов'язковий") String region) {
        return ResponseEntity.ok(service.getWeatherWeekly(region));
    }

    @GetMapping("/daily")
    public ResponseEntity<WeatherDailyDto> getWeatherDaily(
            @RequestParam @NotBlank(message = "Регіон обов'язковий") String region) {
        return ResponseEntity.ok(service.getWeatherDaily(region));
    }
}

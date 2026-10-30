package ua.garage.lelviv.weather;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.garage.lelviv.weather.entity.WeatherDailyDto;
import ua.garage.lelviv.weather.entity.WeatherHourlyDto;
import ua.garage.lelviv.weather.entity.WeatherRequest;
import ua.garage.lelviv.weather.entity.WeatherWeeklyDto;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService service;

    @GetMapping("/hourly")
    public ResponseEntity<WeatherHourlyDto> getWeatherHourly(@Valid @RequestBody WeatherRequest request) {
        return ResponseEntity.ok(service.getWeatherHourly(request));
    }

    @GetMapping("/weekly")
    public ResponseEntity<WeatherWeeklyDto> getWeatherWeekly(@Valid @RequestBody WeatherRequest request) {
        return ResponseEntity.ok(service.getWeatherWeekly(request));
    }

    @GetMapping("/daily")
    public ResponseEntity<WeatherDailyDto> getWeatherDaily(@Valid @RequestBody WeatherRequest request) {
        return ResponseEntity.ok(service.getWeatherDaily(request));
    }
}

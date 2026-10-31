package ua.garage.lelviv.weather.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ua.garage.lelviv.weather.entity.RegionMap;

@Configuration
@EnableConfigurationProperties({RegionMap.class, WeatherBaseProperties.class})
public class WeatherBaseConfig {}

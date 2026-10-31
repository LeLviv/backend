package ua.garage.lelviv.weather.entity;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.weather")
public record RegionMap(Map<String, Coordinates> region) {}

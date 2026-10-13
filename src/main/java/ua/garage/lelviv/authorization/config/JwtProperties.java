package ua.garage.lelviv.authorization.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.application.security.jwt")
public record JwtProperties(
    String SECRET_KEY
) {}

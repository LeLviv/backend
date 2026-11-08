package ua.garage.lelviv.common.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.application.exception")
public record ExceptionProperties(String baseErrorsUrl) {}

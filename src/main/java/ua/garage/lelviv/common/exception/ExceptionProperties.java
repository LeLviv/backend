package ua.garage.lelviv.common.exception;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("app.exception")
@Validated
public record ExceptionProperties(@URL @NotEmpty String baseErrorsUrl) {}

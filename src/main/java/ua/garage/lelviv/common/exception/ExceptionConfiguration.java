package ua.garage.lelviv.common.exception;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ExceptionProperties.class)
public class ExceptionConfiguration {
}

package ua.garage.lelviv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ua.garage.lelviv.authorization.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@EnableScheduling
public class LelvivApplication {

    public static void main(String[] args) {

        SpringApplication.run(LelvivApplication.class, args);
    }
}

package ua.garage.lelviv.ai.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("app.ai")
@Validated
public record AiBaseProperties(@NotBlank String geminiApiKey) {}

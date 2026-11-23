package ua.garage.lelviv.ai.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DayPlanRequest(
        @NotNull(message = "Час обов'язковий")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime time,

        @NotBlank(message = "Регіон обов'язковий") String region) {}

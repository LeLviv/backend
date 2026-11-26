package ua.garage.lelviv.ai.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "Запит не може бути порожнім") @Size(max = 500, message = "Запит занадто довгий")
        String query) {}

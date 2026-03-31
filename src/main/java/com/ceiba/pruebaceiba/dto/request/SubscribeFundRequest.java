package com.ceiba.pruebaceiba.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SubscribeFundRequest(
        @NotBlank(message = "El clientId es obligatorio")
        String clientId,
        @NotBlank(message = "El fundId es obligatorio")
        String fundId
) {
}

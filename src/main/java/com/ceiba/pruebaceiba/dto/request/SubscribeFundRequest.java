package com.ceiba.pruebaceiba.dto.request;

import jakarta.validation.constraints.NotBlank;


public record SubscribeFundRequest(
        @NotBlank(message = "El clientId es obligatorio")
        String clientId,
        @NotBlank(message = "El fundId es obligatorio")
        String fundId
) {
}

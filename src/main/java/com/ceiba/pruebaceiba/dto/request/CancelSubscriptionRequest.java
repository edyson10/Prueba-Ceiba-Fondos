package com.ceiba.pruebaceiba.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CancelSubscriptionRequest(
        @NotBlank(message = "El subscriptionId es obligatorio")
        String subscriptionId
) {
}

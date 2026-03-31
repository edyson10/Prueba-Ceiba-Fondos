package com.ceiba.pruebaceiba.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        String subscriptionId,
        String clientId,
        String fundId,
        String fundName,
        BigDecimal amount,
        String status,
        BigDecimal currentBalance,
        LocalDateTime openedAt,
        LocalDateTime canceledAt
) {
}

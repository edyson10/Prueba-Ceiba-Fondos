package com.ceiba.pruebaceiba.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        String clientId,
        String fundId,
        String fundName,
        String type,
        BigDecimal amount,
        String status,
        String message,
        LocalDateTime createdAt
) {
}

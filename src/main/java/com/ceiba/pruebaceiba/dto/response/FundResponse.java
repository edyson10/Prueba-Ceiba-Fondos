package com.ceiba.pruebaceiba.dto.response;

import java.math.BigDecimal;

public record FundResponse(
        String id,
        String name,
        BigDecimal minimumAmount,
        String category,
        boolean active
) {
}

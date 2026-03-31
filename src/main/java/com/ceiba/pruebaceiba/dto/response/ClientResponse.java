package com.ceiba.pruebaceiba.dto.response;

import com.ceiba.pruebaceiba.enums.NotificationPreference;

import java.math.BigDecimal;

public record ClientResponse(
        String id,
        String fullName,
        String email,
        String phone,
        NotificationPreference notificationPreference,
        BigDecimal balance,
        String role
) {
}

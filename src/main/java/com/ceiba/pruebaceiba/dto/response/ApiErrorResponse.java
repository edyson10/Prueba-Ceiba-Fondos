package com.ceiba.pruebaceiba.dto.response;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int statusCode,
        String status,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
package com.ceiba.pruebaceiba.dto.response;

public record ApiResponse<T>(
        int statusCode,
        String status,
        String message,
        T data
) {
}

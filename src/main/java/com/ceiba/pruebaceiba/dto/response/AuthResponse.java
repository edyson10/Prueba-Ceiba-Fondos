package com.ceiba.pruebaceiba.dto.response;

public record AuthResponse(
        String token,
        String tokenType,
        String userId,
        String email,
        String role
) {
}

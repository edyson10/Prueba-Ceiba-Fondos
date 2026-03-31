package com.ceiba.pruebaceiba.controller;

import com.ceiba.pruebaceiba.dto.request.ClientRegisterRequest;
import com.ceiba.pruebaceiba.dto.request.LoginRequest;
import com.ceiba.pruebaceiba.dto.response.ApiResponse;
import com.ceiba.pruebaceiba.dto.response.AuthResponse;
import com.ceiba.pruebaceiba.dto.response.ClientResponse;
import com.ceiba.pruebaceiba.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ClientResponse>> register(
            @Valid @RequestBody ClientRegisterRequest request
    ) {
        ClientResponse response = authService.registerClient(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "CREATED",
                        "Cliente registrado correctamente",
                        response
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "OK",
                        "Inicio de sesión exitoso",
                        response
                )
        );
    }
}
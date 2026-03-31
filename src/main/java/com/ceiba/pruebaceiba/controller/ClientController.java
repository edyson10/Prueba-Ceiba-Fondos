package com.ceiba.pruebaceiba.controller;

import com.ceiba.pruebaceiba.dto.response.ApiResponse;
import com.ceiba.pruebaceiba.dto.response.ClientResponse;
import com.ceiba.pruebaceiba.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ClientResponse>> getCurrentClient(Authentication authentication) {
        ClientResponse response = clientService.getByEmail(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "OK",
                        "Cliente consultado correctamente",
                        response
                )
        );
    }
}
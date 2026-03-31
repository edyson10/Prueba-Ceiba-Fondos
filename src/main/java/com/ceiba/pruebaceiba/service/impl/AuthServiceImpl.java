package com.ceiba.pruebaceiba.service.impl;

import com.ceiba.pruebaceiba.dto.request.ClientRegisterRequest;
import com.ceiba.pruebaceiba.dto.request.LoginRequest;
import com.ceiba.pruebaceiba.dto.response.AuthResponse;
import com.ceiba.pruebaceiba.dto.response.ClientResponse;
import com.ceiba.pruebaceiba.enums.Role;
import com.ceiba.pruebaceiba.exception.BusinessException;
import com.ceiba.pruebaceiba.mapper.ClientMapper;
import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.repository.ClientRepository;
import com.ceiba.pruebaceiba.security.JwtService;
import com.ceiba.pruebaceiba.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ClientMapper clientMapper;

    @Value("${app.initial-balance}")
    private BigDecimal initialBalance;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Client client = clientRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(client.getEmail())
                .password(client.getPassword())
                .roles(client.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails, client.getId(), client.getRole().name());

        return new AuthResponse(
                token,
                "Bearer",
                client.getId(),
                client.getEmail(),
                client.getRole().name()
        );
    }

    @Override
    public ClientResponse registerClient(ClientRegisterRequest request) {
        if (clientRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ya existe un usuario registrado con ese email");
        }

        Client client = Client.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .notificationPreference(request.notificationPreference())
                .balance(initialBalance)
                .role(Role.CLIENT)
                .build();

        Client saved = clientRepository.save(client);
        return clientMapper.toResponse(saved);
    }
}

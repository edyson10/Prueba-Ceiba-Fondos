package com.ceiba.pruebaceiba.service.impl;

import com.ceiba.pruebaceiba.dto.request.ClientRegisterRequest;
import com.ceiba.pruebaceiba.dto.request.LoginRequest;
import com.ceiba.pruebaceiba.dto.response.AuthResponse;
import com.ceiba.pruebaceiba.dto.response.ClientResponse;
import com.ceiba.pruebaceiba.enums.NotificationPreference;
import com.ceiba.pruebaceiba.enums.Role;
import com.ceiba.pruebaceiba.exception.BusinessException;
import com.ceiba.pruebaceiba.mapper.ClientMapper;
import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.repository.ClientRepository;
import com.ceiba.pruebaceiba.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private ClientRepository clientRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private ClientMapper clientMapper;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() throws Exception {
        clientRepository = mock(ClientRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        clientMapper = mock(ClientMapper.class);

        authService = new AuthServiceImpl(
                clientRepository,
                passwordEncoder,
                authenticationManager,
                jwtService,
                clientMapper
        );

        Field field = AuthServiceImpl.class.getDeclaredField("initialBalance");
        field.setAccessible(true);
        field.set(authService, new BigDecimal("500000"));
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("cliente@btg.com", "Cliente123");

        Client client = Client.builder()
                .id("client-id")
                .fullName("Cliente Demo")
                .email("cliente@btg.com")
                .password("encoded-password")
                .role(Role.CLIENT)
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(mock(Authentication.class));

        when(clientRepository.findByEmail("cliente@btg.com"))
                .thenReturn(Optional.of(client));

        when(jwtService.generateToken(any(User.class), eq("client-id"), eq("CLIENT")))
                .thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals("Bearer", response.tokenType());
        assertEquals("client-id", response.userId());
        assertEquals("cliente@btg.com", response.email());
        assertEquals("CLIENT", response.role());
    }

    @Test
    void shouldRegisterClientSuccessfully() {
        ClientRegisterRequest request = new ClientRegisterRequest(
                "Edyson Leal",
                "edyson@test.com",
                "3001234567",
                "123456",
                NotificationPreference.EMAIL
        );

        Client savedClient = Client.builder()
                .id("new-client-id")
                .fullName("Edyson Leal")
                .email("edyson@test.com")
                .phone("3001234567")
                .password("encoded-password")
                .notificationPreference(NotificationPreference.EMAIL)
                .balance(new BigDecimal("500000"))
                .role(Role.CLIENT)
                .build();

        ClientResponse expectedResponse = new ClientResponse(
                "new-client-id",
                "Edyson Leal",
                "edyson@test.com",
                "3001234567",
                NotificationPreference.EMAIL,
                new BigDecimal("500000"),
                "CLIENT"
        );

        when(clientRepository.existsByEmail("edyson@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(clientMapper.toResponse(savedClient)).thenReturn(expectedResponse);

        ClientResponse response = authService.registerClient(request);

        assertNotNull(response);
        assertEquals("new-client-id", response.id());
        assertEquals("edyson@test.com", response.email());
        assertEquals("CLIENT", response.role());
    }

    @Test
    void shouldThrowBusinessExceptionWhenEmailAlreadyExists() {
        ClientRegisterRequest request = new ClientRegisterRequest(
                "Edyson Leal",
                "edyson@test.com",
                "3001234567",
                "123456",
                NotificationPreference.EMAIL
        );

        when(clientRepository.existsByEmail("edyson@test.com")).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> authService.registerClient(request)
        );

        assertEquals("Ya existe un usuario registrado con ese email", exception.getMessage());
    }
}

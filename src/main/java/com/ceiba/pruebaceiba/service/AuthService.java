package com.ceiba.pruebaceiba.service;

import com.ceiba.pruebaceiba.dto.request.ClientRegisterRequest;
import com.ceiba.pruebaceiba.dto.request.LoginRequest;
import com.ceiba.pruebaceiba.dto.response.AuthResponse;
import com.ceiba.pruebaceiba.dto.response.ClientResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    ClientResponse registerClient(ClientRegisterRequest request);
}

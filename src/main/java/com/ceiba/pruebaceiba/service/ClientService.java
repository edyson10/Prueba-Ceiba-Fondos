package com.ceiba.pruebaceiba.service;

import com.ceiba.pruebaceiba.dto.response.ClientResponse;

public interface ClientService {
    ClientResponse getByEmail(String email);
}

package com.ceiba.pruebaceiba.mapper;

import com.ceiba.pruebaceiba.dto.response.ClientResponse;
import com.ceiba.pruebaceiba.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "role", expression = "java(client.getRole().name())")
    ClientResponse toResponse(Client client);
}

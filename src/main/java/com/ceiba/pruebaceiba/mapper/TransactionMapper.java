package com.ceiba.pruebaceiba.mapper;

import com.ceiba.pruebaceiba.dto.response.TransactionResponse;
import com.ceiba.pruebaceiba.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "type", expression = "java(transaction.getType().name())")
    TransactionResponse toResponse(Transaction transaction);
}

package com.ceiba.pruebaceiba.mapper;

import com.ceiba.pruebaceiba.dto.response.SubscriptionResponse;
import com.ceiba.pruebaceiba.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "subscriptionId", source = "subscription.id")
    @Mapping(target = "status", expression = "java(subscription.getStatus().name())")
    @Mapping(target = "currentBalance", source = "currentBalance")
    SubscriptionResponse toResponse(Subscription subscription, BigDecimal currentBalance);
}

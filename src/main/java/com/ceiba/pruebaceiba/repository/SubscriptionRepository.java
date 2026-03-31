package com.ceiba.pruebaceiba.repository;

import com.ceiba.pruebaceiba.enums.SubscriptionStatus;
import com.ceiba.pruebaceiba.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Optional<Subscription> findByClientIdAndFundIdAndStatus(String clientId, String fundId, SubscriptionStatus status);

    Optional<Subscription> findByIdAndStatus(String id, SubscriptionStatus status);

    List<Subscription> findByClientId(String clientId);
}

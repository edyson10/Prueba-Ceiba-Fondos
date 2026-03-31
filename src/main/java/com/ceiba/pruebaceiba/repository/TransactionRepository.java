package com.ceiba.pruebaceiba.repository;

import com.ceiba.pruebaceiba.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByClientIdOrderByCreatedAtDesc(String clientId);
}

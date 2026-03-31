package com.ceiba.pruebaceiba.repository;

import com.ceiba.pruebaceiba.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FundRepository extends MongoRepository<Fund, String> {
    List<Fund> findByActiveTrue();
}
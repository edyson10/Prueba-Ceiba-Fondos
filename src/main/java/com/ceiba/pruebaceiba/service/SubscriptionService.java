package com.ceiba.pruebaceiba.service;

import com.ceiba.pruebaceiba.dto.request.CancelSubscriptionRequest;
import com.ceiba.pruebaceiba.dto.request.SubscribeFundRequest;
import com.ceiba.pruebaceiba.dto.response.SubscriptionResponse;
import com.ceiba.pruebaceiba.dto.response.TransactionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse subscribe(SubscribeFundRequest request);
    SubscriptionResponse cancel(CancelSubscriptionRequest request);
    List<TransactionResponse> getTransactionHistory(String clientId);
}

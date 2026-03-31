package com.ceiba.pruebaceiba.service;

import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.model.Fund;

public interface NotificationService {
    void sendSubscriptionNotification(Client client, Fund fund);
}

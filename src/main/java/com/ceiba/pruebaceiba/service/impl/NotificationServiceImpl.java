package com.ceiba.pruebaceiba.service.impl;

import com.ceiba.pruebaceiba.enums.NotificationPreference;
import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.model.Fund;
import com.ceiba.pruebaceiba.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendSubscriptionNotification(Client client, Fund fund) {
        String message = "Se ha realizado la suscripción al fondo " + fund.getName();

        if (client.getNotificationPreference() == NotificationPreference.EMAIL) {
            log.info("EMAIL enviado a {} con mensaje: {}", client.getEmail(), message);
        } else {
            log.info("SMS enviado a {} con mensaje: {}", client.getPhone(), message);
        }
    }
}

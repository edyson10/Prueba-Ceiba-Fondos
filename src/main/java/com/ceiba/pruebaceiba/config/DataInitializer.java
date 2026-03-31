package com.ceiba.pruebaceiba.config;

import com.ceiba.pruebaceiba.enums.NotificationPreference;
import com.ceiba.pruebaceiba.enums.Role;
import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.model.Fund;
import com.ceiba.pruebaceiba.repository.ClientRepository;
import com.ceiba.pruebaceiba.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FundRepository fundRepository;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.initial-balance}")
    private BigDecimal initialBalance;

    @Override
    public void run(String... args) {
        seedFunds();
        seedUsers();
    }

    private void seedFunds() {
        if (fundRepository.count() > 0) {
            return;
        }

        List<Fund> funds = List.of(
                Fund.builder().id("1").name("FPV_BTG_PACTUAL_RECAUDADORA").minimumAmount(new BigDecimal("75000")).category("FPV").active(true).build(),
                Fund.builder().id("2").name("FPV_BTG_PACTUAL_ECOPETROL").minimumAmount(new BigDecimal("125000")).category("FPV").active(true).build(),
                Fund.builder().id("3").name("DEUDAPRIVADA").minimumAmount(new BigDecimal("50000")).category("FIC").active(true).build(),
                Fund.builder().id("4").name("FDO-ACCIONES").minimumAmount(new BigDecimal("250000")).category("FIC").active(true).build(),
                Fund.builder().id("5").name("FPV_BTG_PACTUAL_DINAMICA").minimumAmount(new BigDecimal("100000")).category("FPV").active(true).build()
        );

        fundRepository.saveAll(funds);
    }

    private void seedUsers() {
        if (!clientRepository.existsByEmail("admin@btg.com")) {
            clientRepository.save(Client.builder()
                    .fullName("Administrador BTG")
                    .email("admin@btg.com")
                    .phone("3000000000")
                    .password(passwordEncoder.encode("Admin123"))
                    .notificationPreference(NotificationPreference.EMAIL)
                    .balance(initialBalance)
                    .role(Role.ADMIN)
                    .build());
        }

        if (!clientRepository.existsByEmail("cliente@btg.com")) {
            clientRepository.save(Client.builder()
                    .fullName("Cliente Demo")
                    .email("cliente@btg.com")
                    .phone("3001234567")
                    .password(passwordEncoder.encode("Cliente123"))
                    .notificationPreference(NotificationPreference.EMAIL)
                    .balance(initialBalance)
                    .role(Role.CLIENT)
                    .build());
        }
    }
}
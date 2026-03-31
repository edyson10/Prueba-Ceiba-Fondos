package com.ceiba.pruebaceiba.service.impl;

import com.ceiba.pruebaceiba.dto.request.CancelSubscriptionRequest;
import com.ceiba.pruebaceiba.dto.request.SubscribeFundRequest;
import com.ceiba.pruebaceiba.dto.response.SubscriptionResponse;
import com.ceiba.pruebaceiba.dto.response.TransactionResponse;
import com.ceiba.pruebaceiba.enums.SubscriptionStatus;
import com.ceiba.pruebaceiba.enums.TransactionType;
import com.ceiba.pruebaceiba.exception.BusinessException;
import com.ceiba.pruebaceiba.exception.InsufficientBalanceException;
import com.ceiba.pruebaceiba.exception.ResourceNotFoundException;
import com.ceiba.pruebaceiba.mapper.SubscriptionMapper;
import com.ceiba.pruebaceiba.mapper.TransactionMapper;
import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.model.Fund;
import com.ceiba.pruebaceiba.model.Subscription;
import com.ceiba.pruebaceiba.model.Transaction;
import com.ceiba.pruebaceiba.repository.ClientRepository;
import com.ceiba.pruebaceiba.repository.FundRepository;
import com.ceiba.pruebaceiba.repository.SubscriptionRepository;
import com.ceiba.pruebaceiba.repository.TransactionRepository;
import com.ceiba.pruebaceiba.service.NotificationService;
import com.ceiba.pruebaceiba.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final TransactionMapper transactionMapper;
    private final NotificationService notificationService;

    @Override
    public SubscriptionResponse subscribe(SubscribeFundRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        Fund fund = fundRepository.findById(request.fundId())
                .orElseThrow(() -> new ResourceNotFoundException("Fondo no encontrado"));

        validateSufficientBalance(client, fund);

        subscriptionRepository.findByClientIdAndFundIdAndStatus(
                client.getId(),
                fund.getId(),
                SubscriptionStatus.ACTIVE
        ).ifPresent(subscription -> {
            throw new BusinessException("El cliente ya tiene una suscripción activa a este fondo");
        });

        client.setBalance(client.getBalance().subtract(fund.getMinimumAmount()));
        clientRepository.save(client);

        Subscription subscription = Subscription.builder()
                .clientId(client.getId())
                .fundId(fund.getId())
                .fundName(fund.getName())
                .amount(fund.getMinimumAmount())
                .status(SubscriptionStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        saveTransaction(
                client.getId(),
                fund.getId(),
                fund.getName(),
                TransactionType.OPENING,
                fund.getMinimumAmount(),
                "SUCCESS",
                "Suscripción realizada correctamente"
        );

        notificationService.sendSubscriptionNotification(client, fund);

        return subscriptionMapper.toResponse(savedSubscription, client.getBalance());
    }

    @Override
    public SubscriptionResponse cancel(CancelSubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findByIdAndStatus(
                        request.subscriptionId(),
                        SubscriptionStatus.ACTIVE
                )
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción activa no encontrada"));

        Client client = clientRepository.findById(subscription.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscription.setCanceledAt(LocalDateTime.now());
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        client.setBalance(client.getBalance().add(subscription.getAmount()));
        clientRepository.save(client);

        saveTransaction(
                client.getId(),
                subscription.getFundId(),
                subscription.getFundName(),
                TransactionType.CANCELLATION,
                subscription.getAmount(),
                "SUCCESS",
                "Cancelación realizada correctamente"
        );

        return subscriptionMapper.toResponse(savedSubscription, client.getBalance());
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(String clientId) {
        return transactionRepository.findByClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    private void validateSufficientBalance(Client client, Fund fund) {
        if (client.getBalance().compareTo(fund.getMinimumAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "No tiene saldo disponible para vincularse al fondo " + fund.getName()
            );
        }
    }

    private void saveTransaction(
            String clientId,
            String fundId,
            String fundName,
            TransactionType type,
            BigDecimal amount,
            String status,
            String message
    ) {
        Transaction transaction = Transaction.builder()
                .clientId(clientId)
                .fundId(fundId)
                .fundName(fundName)
                .type(type)
                .amount(amount)
                .status(status)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }
}
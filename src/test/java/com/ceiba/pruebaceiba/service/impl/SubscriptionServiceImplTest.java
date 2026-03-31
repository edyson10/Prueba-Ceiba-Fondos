package com.ceiba.pruebaceiba.service.impl;

import com.ceiba.pruebaceiba.dto.request.CancelSubscriptionRequest;
import com.ceiba.pruebaceiba.dto.request.SubscribeFundRequest;
import com.ceiba.pruebaceiba.dto.response.SubscriptionResponse;
import com.ceiba.pruebaceiba.enums.NotificationPreference;
import com.ceiba.pruebaceiba.enums.Role;
import com.ceiba.pruebaceiba.enums.SubscriptionStatus;
import com.ceiba.pruebaceiba.exception.InsufficientBalanceException;
import com.ceiba.pruebaceiba.exception.ResourceNotFoundException;
import com.ceiba.pruebaceiba.mapper.SubscriptionMapper;
import com.ceiba.pruebaceiba.mapper.TransactionMapper;
import com.ceiba.pruebaceiba.model.Client;
import com.ceiba.pruebaceiba.model.Fund;
import com.ceiba.pruebaceiba.model.Subscription;
import com.ceiba.pruebaceiba.repository.ClientRepository;
import com.ceiba.pruebaceiba.repository.FundRepository;
import com.ceiba.pruebaceiba.repository.SubscriptionRepository;
import com.ceiba.pruebaceiba.repository.TransactionRepository;
import com.ceiba.pruebaceiba.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionServiceImplTest {

    private ClientRepository clientRepository;
    private FundRepository fundRepository;
    private SubscriptionRepository subscriptionRepository;
    private TransactionRepository transactionRepository;
    private SubscriptionMapper subscriptionMapper;
    private TransactionMapper transactionMapper;
    private NotificationService notificationService;

    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        fundRepository = mock(FundRepository.class);
        subscriptionRepository = mock(SubscriptionRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        subscriptionMapper = mock(SubscriptionMapper.class);
        transactionMapper = mock(TransactionMapper.class);
        notificationService = mock(NotificationService.class);

        subscriptionService = new SubscriptionServiceImpl(
                clientRepository,
                fundRepository,
                subscriptionRepository,
                transactionRepository,
                subscriptionMapper,
                transactionMapper,
                notificationService
        );
    }

    @Test
    void shouldSubscribeSuccessfully() {
        Client client = Client.builder()
                .id("client-1")
                .fullName("Cliente Demo")
                .email("cliente@btg.com")
                .phone("3001234567")
                .password("encoded")
                .notificationPreference(NotificationPreference.EMAIL)
                .balance(new BigDecimal("500000"))
                .role(Role.CLIENT)
                .build();

        Fund fund = Fund.builder()
                .id("1")
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .minimumAmount(new BigDecimal("75000"))
                .category("FPV")
                .active(true)
                .build();

        Subscription savedSubscription = Subscription.builder()
                .id("sub-1")
                .clientId("client-1")
                .fundId("1")
                .fundName("FPV_BTG_PACTUAL_RECAUDADORA")
                .amount(new BigDecimal("75000"))
                .status(SubscriptionStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .build();

        SubscriptionResponse expectedResponse = new SubscriptionResponse(
                "sub-1",
                "client-1",
                "1",
                "FPV_BTG_PACTUAL_RECAUDADORA",
                new BigDecimal("75000"),
                "ACTIVE",
                new BigDecimal("425000"),
                savedSubscription.getOpenedAt(),
                null
        );

        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(fundRepository.findById("1")).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByClientIdAndFundIdAndStatus(
                "client-1", "1", SubscriptionStatus.ACTIVE)
        ).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(savedSubscription);
        when(subscriptionMapper.toResponse(savedSubscription, new BigDecimal("425000")))
                .thenReturn(expectedResponse);

        SubscriptionResponse response = subscriptionService.subscribe(
                new SubscribeFundRequest("client-1", "1")
        );

        assertNotNull(response);
        assertEquals("sub-1", response.subscriptionId());
        assertEquals(new BigDecimal("425000"), response.currentBalance());

        verify(clientRepository).save(any(Client.class));
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(transactionRepository).save(any());
        verify(notificationService).sendSubscriptionNotification(any(Client.class), any(Fund.class));
    }

    @Test
    void shouldThrowInsufficientBalanceException() {
        Client client = Client.builder()
                .id("client-1")
                .balance(new BigDecimal("50000"))
                .build();

        Fund fund = Fund.builder()
                .id("4")
                .name("FDO-ACCIONES")
                .minimumAmount(new BigDecimal("250000"))
                .build();

        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(fundRepository.findById("4")).thenReturn(Optional.of(fund));

        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> subscriptionService.subscribe(
                        new SubscribeFundRequest("client-1", "4")
                )
        );

        assertEquals(
                "No tiene saldo disponible para vincularse al fondo FDO-ACCIONES",
                exception.getMessage()
        );
    }

    @Test
    void shouldCancelSubscriptionSuccessfully() {
        Subscription subscription = Subscription.builder()
                .id("sub-1")
                .clientId("client-1")
                .fundId("1")
                .fundName("FPV_BTG_PACTUAL_RECAUDADORA")
                .amount(new BigDecimal("75000"))
                .status(SubscriptionStatus.ACTIVE)
                .openedAt(LocalDateTime.now())
                .build();

        Client client = Client.builder()
                .id("client-1")
                .balance(new BigDecimal("425000"))
                .build();

        Subscription updatedSubscription = Subscription.builder()
                .id("sub-1")
                .clientId("client-1")
                .fundId("1")
                .fundName("FPV_BTG_PACTUAL_RECAUDADORA")
                .amount(new BigDecimal("75000"))
                .status(SubscriptionStatus.CANCELED)
                .openedAt(subscription.getOpenedAt())
                .canceledAt(LocalDateTime.now())
                .build();

        SubscriptionResponse expectedResponse = new SubscriptionResponse(
                "sub-1",
                "client-1",
                "1",
                "FPV_BTG_PACTUAL_RECAUDADORA",
                new BigDecimal("75000"),
                "CANCELED",
                new BigDecimal("500000"),
                updatedSubscription.getOpenedAt(),
                updatedSubscription.getCanceledAt()
        );

        when(subscriptionRepository.findByIdAndStatus("sub-1", SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.of(subscription));
        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(updatedSubscription);
        when(subscriptionMapper.toResponse(updatedSubscription, new BigDecimal("500000")))
                .thenReturn(expectedResponse);

        SubscriptionResponse response = subscriptionService.cancel(
                new CancelSubscriptionRequest("sub-1")
        );

        assertNotNull(response);
        assertEquals("CANCELED", response.status());
        assertEquals(new BigDecimal("500000"), response.currentBalance());

        verify(clientRepository).save(any(Client.class));
        verify(transactionRepository).save(any());
    }

    @Test
    void shouldThrowResourceNotFoundWhenCancelingNonExistingSubscription() {
        when(subscriptionRepository.findByIdAndStatus("sub-404", SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> subscriptionService.cancel(new CancelSubscriptionRequest("sub-404"))
        );

        assertEquals("Suscripción activa no encontrada", exception.getMessage());
    }
}
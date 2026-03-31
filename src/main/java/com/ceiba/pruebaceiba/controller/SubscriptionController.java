package com.ceiba.pruebaceiba.controller;

import com.ceiba.pruebaceiba.dto.request.CancelSubscriptionRequest;
import com.ceiba.pruebaceiba.dto.request.SubscribeFundRequest;
import com.ceiba.pruebaceiba.dto.response.ApiResponse;
import com.ceiba.pruebaceiba.dto.response.SubscriptionResponse;
import com.ceiba.pruebaceiba.dto.response.TransactionResponse;
import com.ceiba.pruebaceiba.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionResponse>> subscribe(
            @Valid @RequestBody SubscribeFundRequest request
    ) {
        SubscriptionResponse response = subscriptionService.subscribe(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "CREATED",
                        "Suscripción realizada correctamente",
                        response
                ));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> cancel(
            @Valid @RequestBody CancelSubscriptionRequest request
    ) {
        SubscriptionResponse response = subscriptionService.cancel(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "OK",
                        "Cancelación realizada correctamente",
                        response
                )
        );
    }

    @GetMapping("/transactions/{clientId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> history(
            @PathVariable String clientId
    ) {
        List<TransactionResponse> response = subscriptionService.getTransactionHistory(clientId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "OK",
                        "Historial consultado correctamente",
                        response
                )
        );
    }
}

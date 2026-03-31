package com.ceiba.pruebaceiba.model;

import com.ceiba.pruebaceiba.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;
    private String clientId;
    private String fundId;
    private String fundName;
    private BigDecimal amount;
    private SubscriptionStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime canceledAt;
}

package com.ceiba.pruebaceiba.model;

import com.ceiba.pruebaceiba.enums.NotificationPreference;
import com.ceiba.pruebaceiba.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
public class Client {

    @Id
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private NotificationPreference notificationPreference;
    private BigDecimal balance;
    private Role role;
}

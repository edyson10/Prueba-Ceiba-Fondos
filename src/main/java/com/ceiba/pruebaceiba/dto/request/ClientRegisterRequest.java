package com.ceiba.pruebaceiba.dto.request;

import com.ceiba.pruebaceiba.enums.NotificationPreference;
import jakarta.validation.constraints.*;

public record ClientRegisterRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String fullName,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es válido")
        String email,
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
        String phone,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,
        @NotNull(message = "La preferencia de notificación es obligatoria")
        NotificationPreference notificationPreference
) {
}

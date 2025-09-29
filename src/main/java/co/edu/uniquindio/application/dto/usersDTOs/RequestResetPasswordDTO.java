package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestResetPasswordDTO(
        @NotBlank(message = "El email es requerido")
        @Email(message = "Email inválido")
        String email
) {}

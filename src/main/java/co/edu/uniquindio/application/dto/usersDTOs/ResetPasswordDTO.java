package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO (
    @NotBlank(message = "El código es requerido")
    String code,
    @Email @NotBlank(message = "El email es requerido")
    String email,
    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String newPassword
            ){}


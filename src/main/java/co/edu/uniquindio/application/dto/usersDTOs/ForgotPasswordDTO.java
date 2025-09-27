package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(@NotBlank @Email String email) {
}

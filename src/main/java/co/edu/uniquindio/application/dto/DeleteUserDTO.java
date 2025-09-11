package co.edu.uniquindio.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeleteUserDTO(@NotNull @NotEmpty String password) {
}

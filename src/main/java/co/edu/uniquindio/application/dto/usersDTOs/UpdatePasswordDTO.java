package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdatePasswordDTO(@NotBlank @Length(min = 7, max = 20) String old_password, @NotBlank @Length(min = 7, max = 20) String new_password) {
}

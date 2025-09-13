package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record UpdatePasswordDTO(@NotBlank @Size (min = 7, max = 20) String old_password, @NotBlank @Length(min = 7, max = 20) String new_password) {
}

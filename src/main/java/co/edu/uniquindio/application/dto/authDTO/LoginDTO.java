package co.edu.uniquindio.application.dto.authDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginDTO(@NotNull @Email String email,
                       @NotNull @Length(min = 7, max = 20) String password) {
}

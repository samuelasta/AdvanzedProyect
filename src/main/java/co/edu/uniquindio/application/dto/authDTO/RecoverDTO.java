package co.edu.uniquindio.application.dto.authDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RecoverDTO(@Email String email,
                         @NotBlank @Length(max = 6) String code,
                         @NotBlank @Length(min = 7, max = 20) String new_password
) {
}

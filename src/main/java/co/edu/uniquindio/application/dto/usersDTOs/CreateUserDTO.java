package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

public record CreateUserDTO(@NotBlank(message = "Nombre requerido")
                           String name,
                            @NotBlank(message = "Apellido requerido")
                            String surname,
                            @Email(message = "Email inválido")
                            @NotBlank(message = "Email requerido")
                            String email,
                            @NotBlank(message = "Teléfono requerido")
                            String phone,
                            @NotNull(message = "Fecha de nacimiento requerida")
                            LocalDate birthDate,
                            @NotBlank(message = "País requerido")
                            String country,
                            @NotBlank(message = "Contraseña requerida")
                            @Size(min = 6, message = "Contraseña debe tener al menos 6 caracteres")
                            String password
                           ) {


}
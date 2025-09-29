package co.edu.uniquindio.application.dto.usersDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "Nombre requerido")
    private String name;

    @NotBlank(message = "Apellido requerido")
    private String surname;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email requerido")
    private String email;

    @NotBlank(message = "Teléfono requerido")
    private String phone;

    @NotNull(message = "Fecha de nacimiento requerida")
    private LocalDate birthDate;

    @NotBlank(message = "País requerido")
    private String country;

    @NotBlank(message = "Contraseña requerida")
    @Size(min = 6, message = "Contraseña debe tener al menos 6 caracteres")
    private String password;

}
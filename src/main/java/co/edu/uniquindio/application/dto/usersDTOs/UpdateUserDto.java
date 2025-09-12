package co.edu.uniquindio.application.dto.usersDTOs;

import co.edu.uniquindio.application.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record UpdateUserDto(@NotBlank @Length(max = 100) String name,
                            @Length(max = 10) String phone,
                            @Length(max = 300) String photoUrl,
                            @NotNull @Past LocalDate BirthDay,
                            @NotNull Role role) {
}

package co.edu.uniquindio.application.dto.usersDTOs;

import co.edu.uniquindio.application.model.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDTO(String name,
                      String phone,
                      String email,
                      String photoUrl,
                      LocalDate BirthDate,
                      Role role
) {
}

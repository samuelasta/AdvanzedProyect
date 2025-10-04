package co.edu.uniquindio.application.dto.usersDTOs;

import co.edu.uniquindio.application.model.enums.Role;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserDTO(String name,
                      String photoUrl,
                      LocalDate BirthDate,
                      Role role,
                      LocalDateTime createdAt
) {
}

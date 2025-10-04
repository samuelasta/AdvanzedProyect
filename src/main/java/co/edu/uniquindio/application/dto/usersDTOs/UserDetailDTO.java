package co.edu.uniquindio.application.dto.usersDTOs;

import java.time.LocalDateTime;

public record UserDetailDTO(String id,
                            String name,
                            String photoUrl,
                            LocalDateTime createdAt) {
}

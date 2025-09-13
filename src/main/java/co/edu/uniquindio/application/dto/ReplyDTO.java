package co.edu.uniquindio.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record ReplyDTO(@NotBlank @NotNull @Length(max = 1000) String reply) {
}

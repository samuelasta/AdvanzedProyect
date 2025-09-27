package co.edu.uniquindio.application.dto.commentDTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ReplyDTO(@NotBlank @Length(max = 1000) String reply) {
}

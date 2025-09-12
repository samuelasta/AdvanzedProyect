package co.edu.uniquindio.application.dto.hostDTO;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record HostDTO(@NotNull @Length(max = 500) String description,@NotNull List<String> legal_documents) {
}

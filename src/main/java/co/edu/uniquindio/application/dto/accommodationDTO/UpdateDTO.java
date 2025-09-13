package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.AccommodationType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateDTO(@Size(min = 5, max = 25, message = "El título debe tener entre 5 y 25 caracteres")
                        String title,
                        @Size(min = 20, max = 500, message = "La descripción debe tener entre 20 y 500 caracteres")
                        String description,
                        @Min(1) @Max(60) int capacity,
                        @Positive double price,
                        String country,
                        String department,
                        String city,
                        String neighborhood, //puede ser opcional
                        String street,
                        @Pattern(regexp = "^[0-9A-Za-z]{4,10}$", message = "El código postal no es válido")
                        String postalCode,
                        @NotEmpty @Length List<String> pics_url,
                        List<String> amenities,
                        AccommodationType accommodationType

                        ) {
}

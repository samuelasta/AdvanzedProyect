package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateDTO(@Size(min = 5, max = 25, message = "El título debe tener entre 5 y 25 caracteres")
                        String title,
                        @Size(min = 20, max = 500, message = "La descripción debe tener entre 20 y 500 caracteres")
                        String description,
                        @Positive @Max(60) Integer capacity,
                        @Positive Double price,
                        String country,
                        String department,
                        String city,
                        String neighborhood, //puede ser opcional
                        String street,
                        @Pattern(regexp = "^[0-9A-Za-z]{4,10}$", message = "El código postal no es válido")
                        String postalCode,
                        List<String> pics_url,
                        List<Amenities> amenities,
                        AccommodationType accommodationType

                        ) {
}

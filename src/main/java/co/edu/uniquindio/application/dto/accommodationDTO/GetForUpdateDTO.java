package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.validation.constraints.*;

import java.util.List;

public record GetForUpdateDTO(
                              String id,
                              @Size(min = 5, max = 25, message = "El título debe tener entre 5 y 25 caracteres")
                              String title,
                              @Size(min = 20, max = 500, message = "La descripción debe tener entre 20 y 500 caracteres")
                              String description,
                              @Positive @Max(60) Integer capacity,
                              @NotNull @Positive Double price,
                              @NotEmpty String country,
                              @NotBlank String department,
                              @NotBlank String city,
                              String neighborhood, //puede ser opcional
                              String street,
                              @Pattern(regexp = "^[0-9A-Za-z]{4,10}$", message = "El código postal no es válido")
                              @NotBlank String postalCode,
                              @NotEmpty List<String> pics_url,
                              @NotEmpty List<Amenities> amenities,
                              @NotNull AccommodationType accommodationType,
                              @NotNull float latitude, @NotNull float longitude
) {
}

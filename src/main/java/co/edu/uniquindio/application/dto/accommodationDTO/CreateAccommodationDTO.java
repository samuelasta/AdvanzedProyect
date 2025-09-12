package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateAccommodationDTO(@NotBlank(message = "El título no puede estar vacío")
                                     @Size(min = 5, max = 40, message = "El título debe tener entre 5 y 100 caracteres")
                                     String title,
                                     @NotBlank(message = "La descripción no puede estar vacía")
                                     @Size(min = 20, max = 2000, message = "La descripción debe tener entre 20 y 2000 caracteres")
                                     String description,
                                     @Positive double price,
                                     @NotEmpty List<String> picsUrl,
                                     @NotNull AccommodationType accommodationType,
                                     @Min(1) @Max(60) int capacity,
                                     @NotBlank String country,
                                     @NotBlank String department,
                                     @NotBlank String city,
                                     String neighborhood //puede ser opcional
                                     ,@NotBlank String street,
                                     @NotBlank @Pattern(regexp = "^[0-9A-Za-z]{4,10}$", message = "El código postal no es válido")
                                     String postalCode,
                                     @NotEmpty(message = "debe tener al menos 1 amenidad") List<Amenities> amenities

                                      ) {
}

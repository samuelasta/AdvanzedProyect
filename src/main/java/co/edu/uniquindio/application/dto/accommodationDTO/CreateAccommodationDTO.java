package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateAccommodationDTO(@NotBlank(message = "El título no puede estar vacío")
                                     @Length(min = 5, max = 25, message = "El título debe tener entre 5 y 25 caracteres")
                                     String title,
                                     @NotBlank(message = "La descripción no puede estar vacía")
                                     @NotBlank @Size(min = 20, max = 500, message = "La descripción debe tener entre 20 y 500 caracteres")
                                     String description,
                                     @NotNull @Positive double price,
                                     @NotEmpty @Size(min = 1, max = 10) List<String> picsUrl,
                                     @NotNull(message = "el tipo de alojamiento es obligatorio")
                                     AccommodationType accommodationType,
                                     @Min(1) @Max(60) int capacity,
                                     @NotBlank String country,
                                     @NotBlank String department,
                                     @NotBlank String city,
                                     @Length(max = 20) String neighborhood //puede ser opcional
                                     , String street,
                                     @NotBlank @Pattern(regexp = "^[0-9A-Za-z]{4,10}$", message = "El código postal no es válido")
                                     String postalCode,
                                     @NotEmpty(message = "debe tener al menos 1 amenidad") List<Amenities> amenities,
                                     @NotNull float latitude,  @NotNull float longitude

                                      ) {
}

package co.edu.uniquindio.application.dto.accommodationDTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record ListAccommodationDTO(
                                    @Length String city,
                                    @FutureOrPresent LocalDateTime checkIn,
                                   @Future LocalDateTime checkOut,
                                   int guest_number
) {
}

package co.edu.uniquindio.application.dto.accommodationDTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ListAccommodationDTO(
                                    @Length String city,
                                    @FutureOrPresent LocalDate checkIn,
                                   @Future LocalDate checkOut,
                                   Integer guest_number
) {
}

package co.edu.uniquindio.application.dto.bookingDTO;

import co.edu.uniquindio.application.model.enums.State;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record SearchBookingDTO(State state,
                               LocalDate checkIn,
                               LocalDate checkOut,
                               @Positive @Length(max = 60) Integer guest_number
                               ) {
}

package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.enums.BookingState;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;

public record ListBookingsDTO(
        BookingState bookingState,
        @FutureOrPresent LocalDateTime checkIn,
        @Future LocalDateTime checkOut,
        int guest_number
) {
}

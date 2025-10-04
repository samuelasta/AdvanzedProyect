package co.edu.uniquindio.application.dto.bookingDTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateBookingDTO(@NotNull @FutureOrPresent LocalDateTime checkIn,
                               @NotNull @Future LocalDateTime checkOut,
                               @NotNull int guest_number

) {
}

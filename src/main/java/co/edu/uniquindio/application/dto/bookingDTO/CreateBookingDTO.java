package co.edu.uniquindio.application.dto.bookingDTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateBookingDTO(@NotNull @FutureOrPresent LocalDateTime check_in,
                               @NotNull @Future LocalDateTime check_out,
                               @NotNull int guest_number

) {
}

package co.edu.uniquindio.application.dto.bookingDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record BookingDTO(@Length(max = 100) String title,
                         State state,
                         UserDTO user,
                         LocalDate checkIn,
                         LocalDate checkOut,
                         int guest_number


                         ) {
}

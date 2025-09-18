package co.edu.uniquindio.application.dto.bookingDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;

import java.time.LocalDate;

public record BookingDTO(String title,
                         State state,
                         UserDTO user,
                         LocalDate checkIn,
                         LocalDate checkOut,
                         int guest_number


                         ) {
}

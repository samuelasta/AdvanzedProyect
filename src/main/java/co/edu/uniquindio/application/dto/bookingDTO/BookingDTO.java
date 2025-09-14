package co.edu.uniquindio.application.dto.bookingDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;

public record BookingDTO(String title,
                         State state,
                         UserDTO user,
                         String checkIn,
                         String checkOut,
                         int guest_number,
                         BookingState bookingState


                         ) {
}

package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.BookingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Booking {

    private String id;
    private String checkIn;
    private String checkOut;
    private int guest_number;
    private BookingState bookingState;
    private Accommodation accommodation; //preguntar si sería mejor una relación con la clase
    private User user;  //preguntar si sería mejor una relación con la clase

}

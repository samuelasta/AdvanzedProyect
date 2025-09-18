package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.BookingState;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Booking {

    private String id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guest_number;
    private BookingState bookingState;
    private Accommodation accommodation;
    private User user;
    private LocalDateTime createdAt;

}

package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.enums.BookingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByAccommodationId(String accommodationId);

    List<Booking> findByAccommodationIdAndBookingState(String accommodationId, BookingState bookingState);
}

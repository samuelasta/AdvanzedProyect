package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByAccommodationId(String accommodationId);
}

package co.edu.uniquindio.application.repositories;
//sam
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.enums.BookingState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByAccommodationId(String accommodationId);

    Optional<Booking> findByAccommodationIdAndBookingState(String accommodationId, BookingState bookingState);

    @Query("""
    SELECT b
    FROM Booking b
    WHERE b.accommodation.id = :accommodationId
      AND (:#{#filters.state} IS NULL OR b.bookingState = :#{#filters.state})
      AND (:#{#filters.checkIn} IS NULL OR b.checkIn >= :#{#filters.checkIn})
      AND (:#{#filters.checkOut} IS NULL OR b.checkOut <= :#{#filters.checkOut})
      AND (:#{#filters.guest_number} IS NULL OR b.guest_number = :#{#filters.guest_number})
    ORDER BY b.checkIn DESC
    """)
    Page<Booking> findBookingsByAccommodationWithFilters(
            @Param("accommodationId") String accommodationId,
            @Param("filters") SearchBookingDTO filters,
            Pageable pageable
    );

}

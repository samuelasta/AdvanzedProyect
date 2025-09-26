package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.dto.accommodationDTO.ListAccommodationDTO;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, String> {

    @Query("select p from Accommodation p where p.user.id = :idUser")
    Page<Accommodation> getAccommodations(String idUser, Pageable pageable);

    List<Accommodation> findByState(State state);

    @Query("""
    SELECT a
    FROM Accommodation a
    WHERE (:#{#dto.city} IS NULL OR LOWER(a.location.city) = LOWER(:#{#dto.city}))
      AND (:#{#dto.guest_number} IS NULL OR a.capacity >= :#{#dto.guest_number})
      AND a.state = co.edu.uniquindio.application.model.enums.State.ACTIVE
      AND (:#{#dto.checkIn} IS NULL OR :#{#dto.checkOut} IS NULL OR 
           NOT EXISTS (
               SELECT b FROM Booking b 
               WHERE b.accommodation = a 
               AND b.bookingState IN (co.edu.uniquindio.application.model.enums.BookingState.PENDING, co.edu.uniquindio.application.model.enums.BookingState.COMPLETED)
               AND (
                   (b.checkIn <= :#{#dto.checkIn} AND b.checkOut > :#{#dto.checkIn})
                   OR (b.checkIn < :#{#dto.checkOut} AND b.checkOut >= :#{#dto.checkOut})
                   OR (b.checkIn >= :#{#dto.checkIn} AND b.checkOut <= :#{#dto.checkOut})
               )
           ))
    """)
    Page<Accommodation> searchAccommodations(@Param("dto") ListAccommodationDTO dto, Pageable pageable);

}

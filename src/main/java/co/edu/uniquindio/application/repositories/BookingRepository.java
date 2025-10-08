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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByAccommodationId(String accommodationId);

    //Optional<Booking> findById(String bookingId);

    Optional<Booking> findByAccommodationIdAndBookingState(String accommodationId, BookingState bookingState);

    //se hace el filtro a la base de datos directamente en caso de que vengan filtros
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

    //se hace el filtro a la base de datos directamente en caso de que vengan filtros
    @Query("""
    SELECT b
    FROM Booking b
    WHERE b.user.id = :userId
      AND (:#{#filters.state} IS NULL OR b.bookingState = :#{#filters.state})
      AND (:#{#filters.checkIn} IS NULL OR b.checkIn >= :#{#filters.checkIn})
      AND (:#{#filters.checkOut} IS NULL OR b.checkOut <= :#{#filters.checkOut})
      AND (:#{#filters.guest_number} IS NULL OR b.guest_number = :#{#filters.guest_number})
    ORDER BY b.checkIn DESC
    """)
    Page<Booking> findBookingsByUserWithFilters(
            @Param("userId") String userId,
            @Param("filters") SearchBookingDTO filters,
            Pageable pageable
    );



    //cuenta cuantas reservas tiene el alojamiento
    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.accommodation.id = :accommodationId
      AND (:startDate IS NULL OR b.createdAt >= :startDate)
      AND (:endDate IS NULL OR b.createdAt <= :endDate)
    """)
    long countByAccommodationIdAndBetween(@Param("accommodationId")String accommodationId,
                                          @Param("startDate")LocalDateTime startDate,
                                          @Param("endDate")LocalDateTime endDate);

    //promedio de ocupación del alojamiento (occupancyRate)
    @Query("""
    SELECT COALESCE(SUM(DATEDIFF(b.checkOut, b.checkIn)), 0)
    FROM Booking b
    WHERE b.accommodation.id = :accommodationId
      AND b.bookingState = co.edu.uniquindio.application.model.enums.BookingState.COMPLETED
      AND (:startDate IS NULL OR b.checkIn >= :startDate)
      AND (:endDate IS NULL OR b.checkOut <= :endDate)
""")
    Double findAverageOccupancyByAccommodationId(@Param("accommodationId")String accommodationId,
                                                 @Param("startDate")LocalDateTime startDate,
                                                 @Param("endDate")LocalDateTime endDate);

    // numero de reservas que han sido canceladas
    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.accommodation.id = :accommodationId
      AND b.bookingState = co.edu.uniquindio.application.model.enums.BookingState.CANCELED
      AND (:startDate IS NULL OR b.checkIn >= :startDate)
      AND (:endDate IS NULL OR b.checkOut <= :endDate)
    """)
    int countCancellationsByAccommodationId(@Param("accommodationId")String accommodationId,
                                            @Param("startDate")LocalDateTime startDate,
                                            @Param("endDate")LocalDateTime endDate);

    //promedio de ganancias de el alojamiento (reservas completadas)
    @Query("""
    SELECT COALESCE(SUM(a.price), 0)
    FROM Booking b
    JOIN b.accommodation a
    WHERE a.id = :accommodationId
      AND b.bookingState = co.edu.uniquindio.application.model.enums.BookingState.COMPLETED
      AND (:startDate IS NULL OR b.checkIn >= :startDate)
      AND (:endDate IS NULL OR b.checkOut <= :endDate)
    """)
    Double findAverageRevenueByAccommodationId(@Param("accommodationId")String accommodationId,
                                                LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
    SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
    FROM Booking b
    WHERE b.accommodation.id = :accommodationId
      AND b.checkIn < :checkOut
      AND b.checkOut > :checkIn
""")
    boolean existsOverlappingBooking(
            @Param("accommodationId") String accommodationId,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut);

}


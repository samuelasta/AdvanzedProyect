package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.dto.accommodationDTO.StatsDateDTO;
import co.edu.uniquindio.application.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    Page<Comment> findAllByAccommodationId(String accommodationId, Pageable pageable);

    boolean existsByBookingId(String bookingId);

    @Query("""
    SELECT COALESCE(AVG(c.rating), 0.0)
    FROM Comment c
    WHERE c.accommodation.id = :accommodationId
      AND (:startDate IS NULL OR c.createdAt >= :startDate)
      AND (:endDate IS NULL OR c.createdAt <= :endDate)
    """)
    Double findAverageRatingByAccommodationId(@Param("accommodationId") String accommodationId,
                                              @Param("startDate")LocalDateTime startDate,
                                              @Param("endDate")LocalDateTime endDate);


    //cuenta el numero dde comentarios de un alojamiento
    @Query("""
SELECt COUNT(c)
FROM Comment c
where c.accommodation.id = :accommodationId
AND (:startDate IS NULL OR c.createdAt >= :startDate)
      AND (:endDate IS NULL OR c.createdAt <= :endDate)
""")
   long countByAccommodationId(@Param("accommodationId")String accommodationId,
                               @Param("startDate")LocalDateTime startDate,
                               @Param("endDate")LocalDateTime endDate);

}


package co.edu.uniquindio.application.repositories;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Favorites;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorites, String> {

    void deleteAllByAccommodationId(String accommodationId);

    boolean existsByUserIdAndAccommodationId(String userId, String accommodationId);

    int deleteByUserIdAndAccommodationId(String userId, String accommodationId);

    // asi me evito traer todo, solo me traigo los alojamientos :)
    @Query("""
         SELECT f.accommodation
         FROM Favorites f WHERE f.user.id = :userId
    """)
    Page<Accommodation> findAccommodationsByUserId(@Param("userId") String userId, Pageable pageable);
}

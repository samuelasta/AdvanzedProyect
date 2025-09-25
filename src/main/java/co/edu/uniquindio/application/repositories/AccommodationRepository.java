package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, String> {

    @Query("select p from Accommodation p where p.user.id = :idUser")
    Page<Accommodation> getAccommodations(String idUser, Pageable pageable);

    List<Accommodation> findByState(State state);

}

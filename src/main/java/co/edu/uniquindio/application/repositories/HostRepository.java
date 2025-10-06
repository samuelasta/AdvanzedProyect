package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<HostProfile, String> {

    Optional<HostProfile> findByUserId(String id);
}

package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<HostProfile, String> {
}

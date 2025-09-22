package co.edu.uniquindio.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetCode extends JpaRepository<PasswordResetCode, String> {
}

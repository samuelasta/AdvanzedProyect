package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email(message = "Email inválido") @NotBlank(message = "Email requerido") String email);
}

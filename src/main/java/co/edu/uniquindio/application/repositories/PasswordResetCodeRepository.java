package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.PasswordResetCode;
import co.edu.uniquindio.application.model.User;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, String> {
    Optional<PasswordResetCode> findByCode(String code);

    Optional<PasswordResetCode> findByCodeAndUser(String code, User user);

}

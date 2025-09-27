package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.PasswordResetCode;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, String> {
    PasswordResetCode findByCode(@NotBlank @Length(max = 6) String code);
}

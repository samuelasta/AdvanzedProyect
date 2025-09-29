package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.usersDTOs.RequestResetPasswordDTO;
import co.edu.uniquindio.application.dto.usersDTOs.ResetPasswordDTO;
import co.edu.uniquindio.application.model.PasswordResetCode;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.repositories.PasswordResetCodeRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.PasswordResetService;
import co.edu.uniquindio.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class PasswordResetServiceImpl implements PasswordResetService {
    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final UserService userService;

    private static final long EXPIRATION_MINUTES = 15;

    @Override
    public void requestPasswordReset(RequestResetPasswordDTO requestResetPasswordDTO) throws Exception {

        String code = generarCodigoAleatorio();
        User user = userService.findByEmail(requestResetPasswordDTO.email());

        PasswordResetCode prc = new PasswordResetCode();
        prc.setUsed(false);
        prc.setUser(user);
        prc.setCode(code);
        prc.setCreatedAt(LocalDateTime.now());

        passwordResetCodeRepository.save(prc);

        //enviar el correo

    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {

        User user = userService.findByEmail(resetPasswordDTO.email());


        Optional<PasswordResetCode> code = passwordResetCodeRepository.findByCodeAndUser(resetPasswordDTO.code(), user);


    }

//    @Override
//    public void validateAndResetPassword(String code, User user, String newPassword) {
//        PasswordResetCode resetCode = passwordResetCodeRepository
//                .findByCodeAndUser(code, user)
//                .orElseThrow(() -> new IllegalArgumentException("Código inválido"));
//
//        if (resetCode.isUsed()) {
//            throw new IllegalStateException("Este código ya fue utilizado");
//        }
//
//        if (resetCode.getCreatedAt()
//                .plusMinutes(EXPIRATION_MINUTES)
//                .isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("El código ha expirado");
//        }
//
//        // 🔒 Cambiar contraseña (encriptada en UserService normalmente)
//        user.setPassword(newPassword);
//        userRepository.save(user);
//
//        // ✅ Marcar código como usado
//        resetCode.setUsed(true);
//        passwordResetCodeRepository.save(resetCode);
//    }
//
//    @Override
//    public void generateResetCode(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ese email"));
//
//        // aquí generas y guardas el código
//        PasswordResetCode resetCode = new PasswordResetCode(
//                UUID.randomUUID().toString(),
//                generarCodigoAleatorio(),
//                LocalDateTime.now(),
//                false,
//                user
//        );
//
//        passwordResetCodeRepository.save(resetCode);
//    }
        private String generarCodigoAleatorio() {
        return String.valueOf((int)(Math.random() * 900000) + 100000); // 6 dígitos
        }
}

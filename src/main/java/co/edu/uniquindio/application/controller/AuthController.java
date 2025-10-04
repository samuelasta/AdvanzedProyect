package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.RequestResetPasswordDTO;
import co.edu.uniquindio.application.dto.usersDTOs.ResetPasswordDTO;
import co.edu.uniquindio.application.services.PasswordResetService;
import co.edu.uniquindio.application.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;


    // crear un usuario (hecho)
    @PostMapping
    public ResponseEntity<ResponseDTO<String>> create(@Valid @RequestBody CreateUserDTO createUserDTO) throws Exception {
        userService.create(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "registro exitoso :)"));
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception{
        TokenDTO token = userService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(false, token));
    }

    // solicitar codigo para cambiar la contraseña
    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestReset(@Valid @RequestBody RequestResetPasswordDTO dto) throws Exception{
        passwordResetService.requestPasswordReset(dto);
        return ResponseEntity.ok("Se ha enviado un código de recuperación a tu email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) throws Exception{
        passwordResetService.resetPassword(dto);
        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }



}

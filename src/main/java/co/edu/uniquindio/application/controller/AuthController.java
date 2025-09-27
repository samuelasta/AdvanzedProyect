package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.RecoverDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.ForgotPasswordDTO;
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


    // crear un usuario (hecho)
    @PostMapping
    public ResponseEntity<ResponseDTO<String>> create(@Valid @RequestBody CreateUserDTO createUserDTO) throws Exception {
        userService.create(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "registro exitoso :)"));
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception{
        TokenDTO token = userService.login(loginDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(false, token));
    }

    //recuperar contraseña perdida (hecho)
    @PatchMapping("/recover")
    public ResponseEntity<ResponseDTO<String>> recover_password(@Valid @RequestBody RecoverDTO recoverDTO) throws Exception {
        userService.resetPassword(recoverDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(false, "contraseña actualizada"));
    }

    //método pendiente
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO<String>> sendVerificationCode(@RequestBody ForgotPasswordDTO forgotPasswordDTO) throws Exception{
        //TODO llamar al servicio para enviar el código
        return ResponseEntity.ok(new ResponseDTO<>(false, "Código enviado"));
    }


}

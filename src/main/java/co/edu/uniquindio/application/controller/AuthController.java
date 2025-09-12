package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.RecoverDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // login
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> login(@Valid @RequestBody LoginDTO loginDTO){

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(false, "login exitoso"));
    }
    //recuperar contraseña perdida
    @PatchMapping("/recover")
    public ResponseEntity<ResponseDTO<String>> recover_password(@Valid @RequestBody RecoverDTO recoverDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDTO<>(false, "contraseña recuperada y actualizada"));
    }


}

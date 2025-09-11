package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> create(@RequestBody CreateUserDTO RegisterUserDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "registro exitoso :)"));
    }

    @PutMapping(("/{id}"))
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable String id, @Valid @RequestBody UpdateUserDto updateUserDto) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "actualizacion exitosa :)"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id, @Valid @RequestBody DeleteUserDTO deleteUserDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "eliminacion exitosa :)"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> listAll(@PathVariable String id) throws Exception {
        //Lógica para consultar todos los usuarios
        List<UserDTO> list = new ArrayList<>();
        return ResponseEntity.ok(new ResponseDTO<>(false, list));
    }

}

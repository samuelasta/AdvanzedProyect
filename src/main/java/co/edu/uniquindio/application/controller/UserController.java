package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.*;
import co.edu.uniquindio.application.dto.hostDTO.HostDTO;
import co.edu.uniquindio.application.dto.usersDTOs.*;
import co.edu.uniquindio.application.model.Accommodation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
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

    //actualizar datos del host
    @PutMapping("/{id}/host")
    public ResponseEntity<ResponseDTO<String>> add_data_host(@PathVariable String id, @Valid @RequestBody HostDTO hostDTO ) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "datos añadidos con exito "));
    }

    //cambiar contraseña
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> update_password(@PathVariable String id, @Valid @RequestBody UpdatePasswordDTO updateUserDto) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "contraseña actualizada :)"));
    }

    //ver listado de reservas del cliente
    @GetMapping("/{id}/bookings")
    public ResponseEntity<ResponseDTO<List<UserBookingsListDTO>>> booking_list(@PathVariable String id, @Valid @RequestBody UserBookingsListDTO userBookingsListDTO) throws Exception {

        List<UserBookingsListDTO> list = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }


    //devuelve lista de todos los usuarios
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> listAll(@PathVariable String id) throws Exception {
        //Lógica para consultar todos los usuarios
        List<UserDTO> list = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //lista de alojamientos del host
    @GetMapping("/{id}/accommodations/host")
    public ResponseEntity<ResponseDTO<List<Accommodation>>> listAccommodationHost(@PathVariable String id) throws Exception {
        List<Accommodation> list = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false,list));
    }

}

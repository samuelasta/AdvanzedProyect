package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.*;
import co.edu.uniquindio.application.dto.hostDTO.HostDTO;
import co.edu.uniquindio.application.dto.usersDTOs.*;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.services.AccommodationService;
import co.edu.uniquindio.application.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.uniquindio.application.dto.accommodationDTO.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccommodationService accommodationService;


    // crear un usuario (hecho)
    @PostMapping
    public ResponseEntity<ResponseDTO<String>> create(@Valid @RequestBody CreateUserDTO createUserDTO) throws Exception {
        userService.create(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "registro exitoso :)"));
    }

    @PutMapping(("/{id}"))
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable String id, @Valid @RequestBody UpdateUserDto updateUserDto) throws Exception {

        userService.edit(id, updateUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "actualizacion exitosa :)"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id, @Valid @RequestBody DeleteUserDTO deleteUserDTO) throws Exception {

        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "eliminacion exitosa :)"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> get(@PathVariable String id) throws Exception{
        UserDTO userDTO = userService.get(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, userDTO));
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
    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> listAll() throws Exception {

        List<UserDTO> list = userService.listAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //lista de alojamientos del host
    @GetMapping("/{id}/accommodations/host")
    public ResponseEntity<ResponseDTO<List<AccommodationDTO>>> listAccommodationHost(@PathVariable String id) throws Exception {
        List<AccommodationDTO> list = accommodationService.listAllAccommodationsHost(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

}

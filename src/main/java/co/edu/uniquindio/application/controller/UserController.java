package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.dto.hostDTO.HostDTO;
import co.edu.uniquindio.application.dto.usersDTOs.*;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.services.AccommodationService;
import co.edu.uniquindio.application.services.BookingService;
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

    private final AccommodationService accommodationService;
    private final UserService userService;
    private final BookingService bookingService;


    @PutMapping(("/{id}"))
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable String id, @Valid @RequestBody UpdateUserDto updateUserDto) throws Exception {
        userService.edit(id, updateUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "actualizacion exitosa :)"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id, @Valid @RequestBody DeleteUserDTO deleteUserDTO) throws Exception {

        userService.delete(id, deleteUserDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "eliminacion exitosa :)"));
    }

    // obtener el usuario
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> get(@PathVariable String id) throws Exception{
        UserDTO userDTO = userService.get(id);
        return ResponseEntity.ok(new ResponseDTO<>(false, userDTO));
    }

    //actualizar datos del host
    @PutMapping("/{id}/host")
    public ResponseEntity<ResponseDTO<String>> add_data_host(@PathVariable String id, @Valid @RequestBody HostDTO hostDTO ) throws Exception {
        userService.addHostData(id, hostDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "datos añadidos con exito "));
    }

    //cambiar contraseña
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> update_password(@PathVariable String id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) throws Exception {
        userService.changePassword(id, updatePasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "contraseña actualizada :)"));
    }

    //ver listado de reservas del cliente(filtros y paginación)
    @GetMapping("/{id}/bookings/{page}")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> booking_list(@PathVariable String id, @PathVariable int page, @Valid @RequestBody SearchBookingDTO searchBookingDTO) throws Exception {
        List<BookingDTO> list = bookingService.listBookingsUser(id, page, searchBookingDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }


    //lista de alojamientos del host
    @GetMapping("/{id}/accommodations/host/{page}")
    public ResponseEntity<ResponseDTO<List<AccommodationDTO>>> listAccommodationHost(@PathVariable String id, @PathVariable int page) throws Exception {
        List<AccommodationDTO> list = accommodationService.listAllAccommodationsHost(id, page);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

}

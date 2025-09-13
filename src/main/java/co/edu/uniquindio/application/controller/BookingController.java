package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.model.enums.BookingState;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    //crear una reserva
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> create(@PathVariable String id, @Valid @RequestBody CreateBookingDTO createBookingDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<String>(false, "reserva creada"));
    }

    //eliminar una reserva (cambiar su estado)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "reserva eliminada"));
    }

}

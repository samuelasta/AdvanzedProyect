package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.services.BookingService;
import co.edu.uniquindio.application.services.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final CurrentUserService currentUserService;

    //crear una reserva (hecho)
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> create(@PathVariable String id, @Valid @RequestBody CreateBookingDTO createBookingDTO) throws Exception {
        String userId = currentUserService.getCurrentUser();
        bookingService.create(id, userId, createBookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<String>(false, "reserva creada"));
    }

    //eliminar una reserva (cambiar su estado), hecho
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id) throws Exception {
        bookingService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "reserva eliminada"));
    }

}

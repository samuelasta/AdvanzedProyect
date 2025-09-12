package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.CreateAccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.ListAccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.ListBookingsDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accommodation")
public class AccommodationController {


    // ver la lista de reservas del alojamiento (aplicando filtros)
    @GetMapping("/{id}/bookings")
    public ResponseEntity<ResponseDTO<String>> booking_list(@PathVariable String id, @Valid @RequestBody ListBookingsDTO listBookingsDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "ahi tienes la lista de reservas del alojamiento"));
    }

    //ver la lista de alojamiento (aplicando filtros)
    @GetMapping
    public ResponseEntity<ResponseDTO<String>> read(@Valid @RequestBody ListAccommodationDTO listAccommodationDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "ahí tienes la lista de alojamientos "));
    }

    //crear el alojamiento
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> create(@PathVariable String id, @Valid @RequestBody CreateAccommodationDTO createAccommodationDTO) throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento creado "));
    }
}

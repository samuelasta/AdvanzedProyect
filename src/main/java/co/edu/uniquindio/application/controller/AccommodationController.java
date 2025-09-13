package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.CreateCommentDTO;
import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/accommodation")
public class AccommodationController {


    // ver la lista de reservas del alojamiento (aplicando filtros)
    @GetMapping("/{AccommodationId}/bookings")
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

    //actualizar alojamiento
    @PutMapping("/{AccommodationId}")
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable String id, @Valid @RequestBody UpdateDTO updateDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento actualizado "));
    }

    //eliminar el alojamiento
    @DeleteMapping("/{AccommodationId}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento eliminado "));
    }

    //listar los servicios del alojamiento
    @GetMapping("/{AccomodationId}/amenities")
    public ResponseEntity<ResponseDTO<List<Amenities>>> listAamenities(@PathVariable String id) throws Exception {
        List<Amenities> list = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    // listar los comentarios del alojamiento
    @GetMapping("/{AccommodationId}/comments")
    public ResponseEntity<ResponseDTO<List<Comment>>> listComments(@PathVariable String id) throws Exception {
        List<Comment> list = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //crear un comentario, acá porque el comentario pertenece al alojamiento
    @PostMapping("/{AccommodationId}/comments")
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable String id, @Valid @RequestBody CreateCommentDTO createCommentDTO) throws Exception{
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, " crear comentario "));
    }

    // mostrar estadisticas del alojamiento
    @GetMapping("/{AccomodationId}/stats")
    public ResponseEntity<ResponseDTO<AccommodationStatsDTO>> stats(@PathVariable String id) throws Exception {
        AccommodationStatsDTO accommodationStatsDTO = null;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodationStatsDTO));
    }


}

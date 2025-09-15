package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.services.AccommodationService;
import co.edu.uniquindio.application.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final CommentService commentService;

    // ver la lista de reservas del alojamiento (aplicando filtros)
    @GetMapping("/{id}/bookings")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> booking_list(@PathVariable String id, @Valid @RequestBody ListBookingsDTO listBookingsDTO) throws Exception {

        List<BookingDTO> list = accommodationService.listAll(listBookingsDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //ver la lista de alojamientos disponibles (aplicando filtros)
    @GetMapping
    public ResponseEntity<ResponseDTO<List<Accommodation>>> read(@Valid @RequestBody ListAccommodationDTO listAccommodationDTO) throws Exception {

        List<Accommodation> list = accommodationService.search(listAccommodationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //crear el alojamiento
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> create(@PathVariable String id, @Valid @RequestBody CreateAccommodationDTO createAccommodationDTO) throws Exception{

        accommodationService.create(id, createAccommodationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento creado "));
    }

    //actualizar alojamiento
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable String id, @Valid @RequestBody UpdateDTO updateDTO) throws Exception {
        accommodationService.edit(id, updateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento actualizado "));
    }

    //eliminar el alojamiento
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id) throws Exception {
        accommodationService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento eliminado "));
    }

    //listar los servicios del alojamiento
    @GetMapping("/{id}/amenities")
    public ResponseEntity<ResponseDTO<List<Amenities>>> listAamenities(@PathVariable String id) throws Exception {
        List<Amenities> list = accommodationService.listAllAmenities(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    // listar los comentarios del alojamiento
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseDTO<List<CommentDTO>>> listComments(@PathVariable String id) throws Exception {
        List<CommentDTO> list = commentService.listComments(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //crear un comentario, acá porque el comentario pertenece al alojamiento
    @PostMapping("/{id}/comments")
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable String id, @Valid @RequestBody CreateCommentDTO createCommentDTO) throws Exception{
        commentService.createComment(id, createCommentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "comentario creado exitosamente"));
    }

    // mostrar estadisticas del alojamiento
    @GetMapping("/{id}/stats")
    public ResponseEntity<ResponseDTO<AccommodationStatsDTO>> stats(@PathVariable String id) throws Exception {

        AccommodationStatsDTO accommodationStatsDTO = accommodationService.stats(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodationStatsDTO));
    }


}

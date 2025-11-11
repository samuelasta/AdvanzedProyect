package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Comment;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.services.AccommodationService;
import co.edu.uniquindio.application.services.BookingService;
import co.edu.uniquindio.application.services.CommentService;
import co.edu.uniquindio.application.services.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final CommentService commentService;
    private final BookingService bookingService;
    private final CurrentUserService currentUserService;


    //ver la lista de alojamientos disponibles (aplicando filtros), hecho
    @GetMapping("/{page}")
    public ResponseEntity<ResponseDTO<List<AccommodationDTO>>> read(@PathVariable int page, @Valid @RequestBody ListAccommodationDTO listAccommodationDTO) throws Exception {
        List<AccommodationDTO> list = accommodationService.search(listAccommodationDTO, page);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //crear el alojamiento (hecho)
    @PostMapping
    public ResponseEntity<ResponseDTO<String>> create( @Valid @RequestBody CreateAccommodationDTO createAccommodationDTO) throws Exception {
        String id = currentUserService.getCurrentUser();
        accommodationService.create(id, createAccommodationDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento creado "));
    }

    //actualizar alojamiento (hecho)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable String id, @Valid @RequestBody UpdateDTO updateDTO) throws Exception {
        accommodationService.update(id, updateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento actualizado "));
    }

    //eliminar el alojamiento (hecho)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable String id) throws Exception {
        accommodationService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "alojamiento eliminado "));
    }

    //listar los servicios del alojamiento (hecho)
    @GetMapping("/{id}/amenities")
    public ResponseEntity<ResponseDTO<List<Amenities>>> listAamenities(@PathVariable String id) throws Exception {
        List<Amenities> list = accommodationService.listAllAmenities(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    // listar los comentarios del alojamiento (hecho)
    @GetMapping("/{id}/comments/{page}")
    public ResponseEntity<ResponseDTO<List<CommentDTO>>> listComments(@PathVariable String id, @PathVariable int page) throws Exception {
        List<CommentDTO> list = commentService.listComments(id, page);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

    //crear un comentario, acá porque el comentario pertenece al alojamiento (hecho)
    @PostMapping("/{bookingId}/comments")
    public ResponseEntity<ResponseDTO<String>> createComment(@PathVariable String bookingId, @Valid @RequestBody CreateCommentDTO createCommentDTO) throws Exception {
        String userId = currentUserService.getCurrentUser();
        commentService.createComment(bookingId, userId, createCommentDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "comentario creado exitosamente"));
    }

    //mostrar todas las reservas del alojamiento aplicando filtros (hecho)
    @GetMapping("/{id}/bookings/{page}")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> listBookings(@PathVariable String id, @PathVariable int page, @Valid @RequestBody SearchBookingDTO searchBookingDTO) throws Exception {
        List<BookingDTO> list = bookingService.listBookings(id, page, searchBookingDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }


    // mostrar estadisticas del alojamiento con rango de fechas (hecho)
    @GetMapping("/{id}/stats")
    public ResponseEntity<ResponseDTO<AccommodationStatsDTO>> stats(@PathVariable String id, @RequestBody StatsDateDTO statsDateDTO) throws Exception {
        AccommodationStatsDTO accommodationStatsDTO = accommodationService.stats(id, statsDateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodationStatsDTO));
    }

    // obtener el alojamiento, cuando un espectador sin loguearse toca en un alojamiento
    @GetMapping("/{id}/detail")
    public ResponseEntity<ResponseDTO<AccommodationDetailDTO>> get(@PathVariable String id) throws Exception {
        AccommodationDetailDTO accommodationDetailDTO = accommodationService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodationDetailDTO));
    }

    @GetMapping("/amenities")
    public ResponseEntity<ResponseDTO<List<Amenities>>> avaliableAccommodations(){
        List<Amenities> amenities = Arrays.asList(Amenities.values());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, amenities));
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<GetForUpdateDTO>> getAccommodationUpdate(@PathVariable String id) throws Exception {
        GetForUpdateDTO accommodation = accommodationService.getForUpdate(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, accommodation));
    }
}
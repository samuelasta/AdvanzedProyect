package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookingService {

    void create(String id, String userId, CreateBookingDTO createBookingDTO) throws Exception;

    void delete(String id) throws Exception;

    //para ver la lista de reservas del alojamiento aplicando filtros y paginación
    List<BookingDTO> listBookings(String id, int page, SearchBookingDTO searchBookingDTO) throws Exception;

    ////para ver la lista de reservas del usuario aplicando filtros y paginación
    List<BookingDTO> listBookingsUser(String id, int page, SearchBookingDTO searchBookingDTO) throws Exception;
}

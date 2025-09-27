package co.edu.uniquindio.application.services.impl;
//samu
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.exceptions.BadRequestException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.BookingMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    public void create(String id, CreateBookingDTO createBookingDTO) throws Exception{

        // vreifica que el checkIn no esté en pasado
       if(createBookingDTO.check_in().isBefore(LocalDateTime.now())){
           throw new BadRequestException("el checkIn es invalido");
       }

       //verifica que el checkIn no esté despues del checkOut
        if(createBookingDTO.check_in().isAfter(createBookingDTO.check_out())) {
            throw new BadRequestException("Datos incorrectos o la fecha de checkIn está despues de la fecha de check Out");
        }

        // verifica si las fechas están disponibles
        boolean avaliable = bookingRepository.existsOverlappingBooking(id, createBookingDTO.check_in(), createBookingDTO.check_out());
        if(avaliable){
            throw new ValueConflictException("fechas no disponibles");
        }

        //verifica que exista el alojamiento
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el alojamiento"));

        //obtenemos el usuario para poder crear la reserva
        User user = accommodation.getUser();

        //la mapeamos y la guardamos en la DB
        Booking booking = bookingMapper.toEntity(createBookingDTO, accommodation, user);
        bookingRepository.save(booking);

    }

    // para cancelar una reserva
    @Override
    public void delete(String id) throws Exception {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new ResourceNotFoundException("No existe este alojamiento");
        }
        if(booking.get().getBookingState().equals(BookingState.PENDING)){
            LocalDateTime checkIn = booking.get().getCheckIn();
            LocalDateTime now = LocalDateTime.now();
            if(now.isBefore(checkIn.minusHours(48))){
                booking.get().setBookingState(BookingState.CANCELED);
                bookingRepository.save(booking.get());
            }
            else{
                throw new ValueConflictException("solo puedes cancelar una reserva 48 horas antes de la fecha de check in");
            }
        }
        else {
            throw new UnauthorizedException("no puedes cancelar esta reserva");
        }
    }

    //lista de todas las reservas de un alojamiento (aplicando filtros y paginación)
    @Override
    public List<BookingDTO> listBookings(String id, int page, SearchBookingDTO searchBookingDTO) throws Exception {

        Optional<User> user = accommodationRepository.findUserByAccommodationId(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("No existe usuario");
        }

        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if (accommodation.isEmpty()) {
            throw new ResourceNotFoundException("No existe el alojamiento");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Booking> bookings = bookingRepository.findBookingsByAccommodationWithFilters(id, searchBookingDTO, pageable);

        return bookings.stream()
                .map(bookingMapper::toBookingDTO)
                .toList();
    }
}

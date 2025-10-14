package co.edu.uniquindio.application.services.impl;
//samu
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.exceptions.*;
import co.edu.uniquindio.application.mappers.BookingMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    private final UserRepository userRepository;
    private final CurrentUserServiceImpl currentUserService;

    @Override
    public void create(String id, String userId, CreateBookingDTO createBookingDTO) throws Exception{

        // vreifica que el checkIn no esté en pasado
       if(createBookingDTO.checkIn().isBefore(LocalDateTime.now())){
           throw new BadRequestException("el checkIn es invalido");
       }

       //verifica que el checkIn no esté despues del checkOut
        if(createBookingDTO.checkIn().isAfter(createBookingDTO.checkOut())) {
            throw new BadRequestException("Datos incorrectos o la fecha de checkIn está despues de la fecha de check Out");
        }

        // verifica si las fechas están disponibles
        boolean avaliable = bookingRepository.existsOverlappingBooking(id, createBookingDTO.checkIn(), createBookingDTO.checkOut());
        if(avaliable){
            throw new ValueConflictException("fechas no disponibles");
        }

        //verifica que exista el alojamiento
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el alojamiento"));

        if(accommodation.getState() == State.INACTIVE){
            throw new ResourceNotFoundException("No existe el alojamiento");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario"));


        //la mapeamos y la guardamos en la DB
        Booking booking = bookingMapper.toEntity(createBookingDTO, accommodation, user);
        bookingRepository.save(booking);

    }

    // para cancelar una reserva
    @Override
    public void delete(String id) throws Exception {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new ResourceNotFoundException("No existe esta reserva");
        }


        if(!Objects.equals(currentUserService.getCurrentUser(), booking.get().getUser().getId())) {
            throw new ForbiddenException("No te pertenece esta reserva");
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

        Optional<Accommodation> accommodation = accommodationRepository.findById(id);

        if (searchBookingDTO.guest_number() != null && searchBookingDTO.guest_number() <= 0) {
            throw new BadRequestException("el numero de huespedes no puede ser menor o 0");
        }
        return getBookingAccommodationDTOS(id, page, searchBookingDTO, accommodation.isEmpty(), accommodation);
    }

    //lista de todas las reservas de un usuario (aplicando filtros y paginación)
    @Override
    public List<BookingDTO> listBookingsUser(String id, int page, SearchBookingDTO searchBookingDTO) throws Exception {

        Optional<User> user = userRepository.findById(id);
        return getBookingUserDTOS(id, page, searchBookingDTO, user.isEmpty(), user);
    }

    @NotNull
    private List<BookingDTO> getBookingUserDTOS(String id, int page, SearchBookingDTO searchBookingDTO, boolean empty, Optional<User> user) {
        if (empty) {
            throw new ResourceNotFoundException("No existe el usuario");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Booking> bookings = bookingRepository.findBookingsByUserWithFilters(id, searchBookingDTO, pageable);

        return bookings.stream()
                .map(bookingMapper::toBookingDTO)
                .toList();
    }

    @NotNull
    private List<BookingDTO> getBookingAccommodationDTOS(String id, int page, SearchBookingDTO searchBookingDTO, boolean empty, Optional<Accommodation> accommodation) {
        if (empty) {
            throw new ResourceNotFoundException("No existe el alojamiento");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Booking> bookings = bookingRepository.findBookingsByAccommodationWithFilters(id, searchBookingDTO, pageable);

        return bookings.stream()
                .map(bookingMapper::toBookingDTO)
                .toList();
    }


}

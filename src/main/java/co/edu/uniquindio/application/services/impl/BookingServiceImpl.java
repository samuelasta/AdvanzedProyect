package co.edu.uniquindio.application.services.impl;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final Map<String, Booking> bookingStore = new ConcurrentHashMap<>();
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    public void create(String id, CreateBookingDTO createBookingDTO) throws Exception{

        if(createBookingDTO.check_in().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Datos incorrectos, el checkIn no puede ser en el pasado");
        }

        if(createBookingDTO.check_in().isAfter(createBookingDTO.check_out())) {
            throw new BadRequestException("Datos incorrectos o la fecha de checkIn está despues de la fecha de check Out");
        }

        List<Booking> list = bookingRepository.findByAccommodationId(id);
        for(Booking booking : list) {
            if(createBookingDTO.check_in().isBefore(booking.getCheckOut())
                    && createBookingDTO.check_out().isAfter(booking.getCheckIn())){
                throw new ValueConflictException("fecha ocupada");
            }
        }
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        User user = accommodation.get().getUser();
        Booking booking = bookingMapper.toEntity(createBookingDTO, accommodation.get(), user);
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

    //lista de todas las reservas de un alojamiento (aplicando filtros)
    @Override
    public List<BookingDTO> listBookings(String id, SearchBookingDTO searchBookingDTO) throws Exception {

        // añadimos a la lista todas las reservas cuyo id sea igual
        List<Booking> bookings = bookingRepository.findByAccommodationId(id);
        List<BookingDTO> bookingDTOs = new ArrayList<>();
        for(Booking booking : bookings) {
            if(booking.getAccommodation().getId().equals(id)) {
                BookingDTO bookingDTO = bookingMapper.toBookingDTO(booking);
                bookingDTOs.add(bookingDTO);
            }
        }
        // eliminamos los que tengan estado diferente (si es que viene estado)
        if(searchBookingDTO.state() != null) {
            Iterator<BookingDTO> iterator = bookingDTOs.iterator();
            while(iterator.hasNext()) {
                if(!Objects.equals(iterator.next().state(), searchBookingDTO.state())) {
                    iterator.remove();
                }
            }
        }
        // eliminamos los que tengan checkIn diferente (si es que viene checkIn)
        if(searchBookingDTO.checkIn() != null) {
            Iterator<BookingDTO> iterator = bookingDTOs.iterator();
            while(iterator.hasNext()){
                if(iterator.next().checkIn().isBefore(searchBookingDTO.checkIn())) {
                    iterator.remove();
                }
            }
        }
        if(searchBookingDTO.checkOut() != null) {
            Iterator<BookingDTO> iterator = bookingDTOs.iterator();
            while(iterator.hasNext()) {
                if(iterator.next().checkOut().isAfter(searchBookingDTO.checkOut())) {
                    iterator.remove();
                }
            }
        }
        if(searchBookingDTO.guest_number() != null){
            Iterator<BookingDTO> iterator = bookingDTOs.iterator();
            while(iterator.hasNext()) {
                if(!Objects.equals(iterator.next().guest_number(), searchBookingDTO.guest_number())) {
                    iterator.remove();
                }
            }
        }

        if(bookingDTOs.isEmpty()) {
            throw new ResourceNotFoundException("No tienes ninguna reserva que aplique");
        }

        return bookingDTOs;
    }
}

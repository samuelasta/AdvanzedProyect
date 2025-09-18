package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.mappers.BookingMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final Map<String, Booking> bookingStore = new ConcurrentHashMap<>();

    @Override
    public void create(String id, CreateBookingDTO createBookingDTO) {
        Booking booking = bookingMapper.toEntity(createBookingDTO);
        bookingStore.put(id, booking);
    }

    @Override
    public void delete(String id) throws Exception {
        Booking booking = bookingStore.get(id);
        if (booking == null) {
            throw new ResourceNotFoundException("No se puede eliminar el booking");
        }
        bookingStore.remove(id);
    }

    @Override
    public List<BookingDTO> listBookings(String id, SearchBookingDTO searchBookingDTO) throws Exception {

        // añadimos a la lista todas las reservas cuyo id sea igual
        List<BookingDTO> list =  new ArrayList<>();
        for(Booking booking : bookingStore.values()) {
            if(booking.getAccommodation().getId().equals(id)) {
                BookingDTO bookingDTO = bookingMapper.toBookingDTO(booking);
                list.add(bookingDTO);
            }
        }
        // eliminamos los que tengan estado diferente (si es que viene estado)
        if(searchBookingDTO.state() != null) {
            Iterator<BookingDTO> iterator = list.iterator();
            while(iterator.hasNext()) {
                if(!Objects.equals(iterator.next().state(), searchBookingDTO.state())) {
                    iterator.remove();
                }
            }
        }
        // eliminamos los que tengan checkIn diferente (si es que viene checkIn)
        if(searchBookingDTO.checkIn() != null) {
            Iterator<BookingDTO> iterator = list.iterator();
            while(iterator.hasNext()){
                if(iterator.next().checkIn().isBefore(searchBookingDTO.checkIn())) {
                    iterator.remove();
                }
            }
        }
        if(searchBookingDTO.checkOut() != null) {
            Iterator<BookingDTO> iterator = list.iterator();
            while(iterator.hasNext()) {
                if(iterator.next().checkOut().isAfter(searchBookingDTO.checkOut())) {
                    iterator.remove();
                }
            }
        }
        if(searchBookingDTO.guest_number() != null){
            Iterator<BookingDTO> iterator = list.iterator();
            while(iterator.hasNext()) {
                if(!Objects.equals(iterator.next().guest_number(), searchBookingDTO.guest_number())) {
                    iterator.remove();
                }
            }
        }

        if(list.isEmpty()) {
            throw new ResourceNotFoundException("No tienes ninguna reserva que aplique");
        }

        return list;
    }
}

package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.AccommodationMapper;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.services.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.application.services.GeoUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final Map<String, Accommodation> accommodationStore = new ConcurrentHashMap<>();

    @Override
    public void create(String id, CreateAccommodationDTO createAccommodationDTO) throws Exception {

        if(verifyExistence(createAccommodationDTO)){
            throw new ValueConflictException("Ya existe este alojamiento");
        }

        Accommodation accommodation1 = accommodationMapper.toEntity(createAccommodationDTO);
        accommodationStore.put(id, accommodation1);

    }

    private boolean verifyExistence(CreateAccommodationDTO createAccommodationDTO) {
        for (Accommodation accommodation : accommodationStore.values()) {
            double distancia = GeoUtils.calcularDistancia(
                    createAccommodationDTO.latitude(), createAccommodationDTO.longitude(),
                    accommodation.getLocation().getCoordinates().latitude(),
                    accommodation.getLocation().getCoordinates().longitude()
            );

            if (distancia <= 10 ) { // 10 metros de umbral
                return true;
            }
        }
        return false;
    }


    @Override
    public void edit(String id, UpdateDTO updateDTO) throws Exception {
        Accommodation accommodation = accommodationStore.get(id);
        if(accommodation == null){
            throw new ResourceNotFoundException("No se encontró el alojamiento");
        }
        if(updateDTO.title() != null && !updateDTO.title().isBlank() ){
            accommodation.setTitle(updateDTO.title());
        }
        if(updateDTO.description() != null){
            accommodation.setDescription(updateDTO.description());
        }
        if(updateDTO.capacity() != 0){
            accommodation.setCapacity(updateDTO.capacity());
        }
        if(updateDTO.price() != null && updateDTO.price() != 0){
            accommodation.setPrice(updateDTO.price());
        }
        if(updateDTO.country() != null && !updateDTO.country().isBlank()){
            accommodation.getLocation().setCountry(updateDTO.country());
        }
        if(updateDTO.department() != null && !updateDTO.department().isBlank()){
            accommodation.getLocation().setDepartment(updateDTO.department());
        }
        if(updateDTO.city() != null && !updateDTO.city().isBlank()){
            accommodation.getLocation().setCity(updateDTO.city());
        }
        if(updateDTO.neighborhood() != null && !updateDTO.neighborhood().isBlank()){
            accommodation.getLocation().setNeighborhood(updateDTO.neighborhood());
        }
        if(updateDTO.street() != null && !updateDTO.street().isBlank()){
            accommodation.getLocation().setStreet(updateDTO.street());
        }
        if(updateDTO.postalCode() != null && !updateDTO.postalCode().isBlank()){
            accommodation.getLocation().setPostalCode(updateDTO.postalCode());
        }
        if(!updateDTO.pics_url().isEmpty()){
            accommodation.setPics_url(updateDTO.pics_url());
        }
        if(!updateDTO.amenities().isEmpty()){
            accommodation.setAmenities(updateDTO.amenities());
        }
        if(updateDTO.accommodationType() != null){
            accommodation.setAccommodationType(updateDTO.accommodationType());
        }

        accommodationStore.put(id, accommodation);
    }

    @Override
    public void delete(String id) throws Exception {

        Accommodation accommodation = accommodationStore.get(id);
        if(accommodation == null){
            throw new ResourceNotFoundException("No existe el alojamiento");
        }
        for(Booking booking : accommodation.getBookings()){
            // preguntar al profesor si checkIn o checkOut
            if(booking.getCheckIn().isAfter(LocalDate.now())){
                throw new ValueConflictException("tienes reservas futuras, no puedes hacer esto");
            }
        }
        accommodationStore.remove(id);
    }

    @Override
    public List<BookingDTO> listAll(ListBookingsDTO listBookingsDTO) throws Exception {
        return List.of();
    }


    @Override
    public List<Accommodation> search(ListAccommodationDTO listAccommodationDTO) throws Exception {
        return List.of();
    }

    @Override
    public List<Amenities> listAllAmenities(String id) throws Exception {

        Accommodation accommodation = accommodationStore.get(id);
        if(accommodation == null){
            throw new ResourceNotFoundException("No se encontró el alojamiento");

        }

        return accommodation.getAmenities();
    }


    @Override
    public AccommodationStatsDTO stats(String id) throws Exception {
        return null;
    }
}

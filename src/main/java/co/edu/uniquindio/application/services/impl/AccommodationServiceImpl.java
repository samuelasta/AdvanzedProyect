package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.AccommodationMapper;
import co.edu.uniquindio.application.mappers.ShowAccommodationMapper;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.services.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.application.services.GeoUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final Map<String, Accommodation> accommodationStore = new ConcurrentHashMap<>();
    private final ShowAccommodationMapper showAccommodationMapper;
    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;

    @Override
    public void create(String id, CreateAccommodationDTO createAccommodationDTO) throws Exception {

       if(verifyExistence(createAccommodationDTO)){
           throw new ValueConflictException("este alojamiento ya existe");
       }
       Accommodation accommodation = accommodationMapper.toEntity(createAccommodationDTO);
       accommodationRepository.save(accommodation);

    }

    private boolean verifyExistence(CreateAccommodationDTO createAccommodationDTO) {

        for (Accommodation accommodation : accommodationStore.values()) {
            double distancia = GeoUtils.calcularDistancia(
                    createAccommodationDTO.latitude(), createAccommodationDTO.longitude(),
                    accommodation.getLocation().getCoordinates().getLatitude(),
                    accommodation.getLocation().getCoordinates().getLongitude()
            );

            if (distancia <= 5 && accommodation.getTitle().equals(createAccommodationDTO.title())) { // 10 metros de umbral
                return true;
            }
        }
        return false;
    }


    //update
    @Override
    public void update(String id, UpdateDTO updateDTO) throws Exception {

        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if(accommodation.isEmpty()){
            throw new ResourceNotFoundException("No se encontró el alojamiento");
        }
        if(updateDTO.title() != null && !updateDTO.title().isBlank() ){
            accommodation.get().setTitle(updateDTO.title());
        }
        if(updateDTO.description() != null){
            accommodation.get().setDescription(updateDTO.description());
        }
        if(updateDTO.capacity() != 0){
            accommodation.get().setCapacity(updateDTO.capacity());
        }
        if(updateDTO.price() != null && updateDTO.price() != 0){
            accommodation.get().setPrice(updateDTO.price());
        }
        if(updateDTO.country() != null && !updateDTO.country().isBlank()){
            accommodation.get().getLocation().setCountry(updateDTO.country());
        }
        if(updateDTO.department() != null && !updateDTO.department().isBlank()){
            accommodation.get().getLocation().setDepartment(updateDTO.department());
        }
        if(updateDTO.city() != null && !updateDTO.city().isBlank()){
            accommodation.get().getLocation().setCity(updateDTO.city());
        }
        if(updateDTO.neighborhood() != null && !updateDTO.neighborhood().isBlank()){
            accommodation.get().getLocation().setNeighborhood(updateDTO.neighborhood());
        }
        if(updateDTO.street() != null && !updateDTO.street().isBlank()){
            accommodation.get().getLocation().setStreet(updateDTO.street());
        }
        if(updateDTO.postalCode() != null && !updateDTO.postalCode().isBlank()){
            accommodation.get().getLocation().setPostalCode(updateDTO.postalCode());
        }
        if(!updateDTO.pics_url().isEmpty()){
            accommodation.get().setPics_url(updateDTO.pics_url());
        }
        if(!updateDTO.amenities().isEmpty()){
            accommodation.get().setAmenities(updateDTO.amenities());
        }
        if(updateDTO.accommodationType() != null){
            accommodation.get().setAccommodationType(updateDTO.accommodationType());
        }

        accommodationRepository.save(accommodation.get());
    }


    //eliminar un alojamiento que no tenga reservas
    @Override
    public void delete(String id) throws Exception {
      Optional<Accommodation> accommodation = accommodationRepository.findById(id);
      if(accommodation.isEmpty()){
          throw new ResourceNotFoundException("No se encontró el alojamiento");
      }
      //me trae todas las reservas del alojamiento cuyo estado sea pendiente
      List<Booking> booking = bookingRepository.findByAccommodationIdAndBookingState(id, BookingState.PENDING);
      if(!booking.isEmpty()){
          throw new UnauthorizedException("no puedes eliminar este alojamiento, tiene reservas pendientes");
      }
      accommodation.get().setState(State.INACTIVE);
      accommodationRepository.save(accommodation.get());
    }


    @Override
    public List<BookingDTO> listAll(ListBookingsDTO listBookingsDTO) throws Exception {


        return List.of();
    }


    // filtro de busqueda de los alojamientos
    @Override
    public List<AccommodationDTO> search(ListAccommodationDTO listAccommodationDTO) throws Exception {

        // Si no viene ningún filtro, devuelvo todos directamente

        List<Accommodation> list = accommodationRepository.findAll();
        if ((listAccommodationDTO.city() == null &&
                listAccommodationDTO.checkIn() == null &&
                listAccommodationDTO.checkOut() == null &&
                listAccommodationDTO.guest_number() == null)) {

            return list.stream()
                    .map(showAccommodationMapper::toAccommodationDTO)
                    .toList();
        }

        if(listAccommodationDTO.city() != null){
           Iterator<Accommodation> iterator = list.iterator();
           while (iterator.hasNext()){
               Accommodation aux = iterator.next();
               if(!aux.getLocation().getCity().equalsIgnoreCase(listAccommodationDTO.city())){
                   iterator.remove();
               }
           }
        }
        if(listAccommodationDTO.checkIn() != null && listAccommodationDTO.checkOut() != null ){
            Iterator<Accommodation> iterator = list.iterator();
            while (iterator.hasNext()){
                Accommodation aux = iterator.next();
                List<Booking> booking = bookingRepository.findByAccommodationId(aux.getId());
                if(booking.isEmpty()){
                    iterator.remove();
                }
                for(Booking booking1: booking){
                     if(booking1.getCheckIn().isBefore(listAccommodationDTO.checkOut()) &&
                        booking1.getCheckOut().isAfter(listAccommodationDTO.checkIn())){
                         iterator.remove();
                     }
                }
            }
        }
        if(listAccommodationDTO.guest_number() != null){
            Iterator<Accommodation> iterator = list.iterator();
            while(iterator.hasNext()){
                Accommodation accommodation = iterator.next();
                if(accommodation.getCapacity() != listAccommodationDTO.guest_number()){
                    iterator.remove();
                }
            }
        }
        if(list.isEmpty()){
            throw new ResourceNotFoundException("No tenemos alojamientos que apliquen");
        }

        return list.stream()
                .map(showAccommodationMapper::toAccommodationDTO)
                .toList();

    }


    // devuelve la lista de todos los servicios del alojamiento
    @Override
    public List<Amenities> listAllAmenities(String id) throws Exception {

        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if(accommodation.isPresent()){
            return accommodation.get().getAmenities();
        }
        // o con este se resume más: .orElseThrow(() -> new ResourceNotFoundException("No se encontraron servicios del alojamiento"));
        throw new ResourceNotFoundException("No se encontraron servicios del alojamiento");
    }


    @Override
    public AccommodationStatsDTO stats(String id) throws Exception {
        return null;
    }

    @Override
    public List<AccommodationDTO> listAllAccommodationsHost(String id) throws Exception {
        return List.of();
    }
}

package co.edu.uniquindio.application.services.impl;
//samu
import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.exceptions.BadRequestException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.AccommodationMapper;
import co.edu.uniquindio.application.mappers.ShowAccommodationMapper;
import co.edu.uniquindio.application.mappers.StatsMapper;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.repositories.CommentRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.application.services.GeoUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final Map<String, Accommodation> accommodationStore = new ConcurrentHashMap<>();
    private final ShowAccommodationMapper showAccommodationMapper;
    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;
    private final StatsMapper statsMapper;


    @Override
    public void create(String id, CreateAccommodationDTO createAccommodationDTO) throws Exception {

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

       if(verifyExistence(createAccommodationDTO)){
           throw new ValueConflictException("este alojamiento ya existe");
       }
       Accommodation accommodation = accommodationMapper.toEntity(createAccommodationDTO);
       accommodation.setUser(optionalUser.get());
       accommodationRepository.save(accommodation);

    }

    private boolean verifyExistence(CreateAccommodationDTO createAccommodationDTO) {

        for (Accommodation accommodation : accommodationRepository.findByState(State.ACTIVE)) {
            double distancia = GeoUtils.calcularDistancia(
                    createAccommodationDTO.latitude(), createAccommodationDTO.longitude(),
                    accommodation.getLocation().getCoordinates().getLatitude(),
                    accommodation.getLocation().getCoordinates().getLongitude()
            );

            if (distancia <= 5 && accommodation.getTitle().equalsIgnoreCase(createAccommodationDTO.title())) { // 10 metros de umbral
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
            throw new ResourceNotFoundException("Accommodation not found");
        }
        accommodationMapper.updateAccommodationFromDto(updateDTO, accommodation.get());

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
      Optional<Booking> booking = bookingRepository.findByAccommodationIdAndBookingState(id, BookingState.PENDING);
      if(booking.isPresent()){
          throw new UnauthorizedException("no puedes eliminar este alojamiento, tiene reservas pendientes");
      }
      accommodation.get().setState(State.INACTIVE);
      accommodationRepository.save(accommodation.get());
    }


    /* ya está en bookingService pero lo dejo comentado (pendiente a revisar)
    //Es para listar las reservas de un alojamiento? si es así deben enviar el id del alojamiento no una lista
    @Override
    public List<BookingDTO> listAll(ListBookingsDTO listBookingsDTO) throws Exception {


        return List.of();
    }*/

    // filtro de busqueda de los alojamientos (rango minimo - maximo precio y servicios added )
    @Override
    public List<AccommodationDTO> search(ListAccommodationDTO listAccommodationDTO, int page) throws Exception {

        if(listAccommodationDTO.minimum() != null && listAccommodationDTO.maximum() != null && listAccommodationDTO.minimum() > listAccommodationDTO.maximum()){
            throw new BadRequestException("el precio minimo no debe superar el precio maximo");
        }
        Pageable pageable = PageRequest.of(page, 10);
        Page<Accommodation> accommodations = accommodationRepository.searchAccommodations(listAccommodationDTO, pageable);

        if(accommodations.isEmpty()){
            throw new ResourceNotFoundException("No hay alojamientos disponibles, prueba otro filtro");
        }
        return accommodations.stream()
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

    //stats del alojamiento, se puede aplicar rango de fechas
    @Override
    public AccommodationStatsDTO stats(String id, StatsDateDTO statsDateDTO) throws Exception {

        double averageRating = commentRepository.findAverageRatingByAccommodationId(id, statsDateDTO.startDate(), statsDateDTO.endDate());
        long totalComments = commentRepository.countByAccommodationId(id, statsDateDTO.startDate(), statsDateDTO.endDate());
        long totalReservations = bookingRepository.countByAccommodationIdAndBetween(id, statsDateDTO.startDate(), statsDateDTO.endDate());
        double occupancyRate = bookingRepository.findAverageOccupancyByAccommodationId(id, statsDateDTO.startDate(), statsDateDTO.endDate());
        int cancellations = bookingRepository.countCancellationsByAccommodationId(id, BookingState.CANCELED, statsDateDTO.startDate(), statsDateDTO.endDate());
        double totalRevenue = bookingRepository.findAverageRevenueByAccommodationId(id, BookingState.COMPLETED, statsDateDTO.startDate(), statsDateDTO.endDate());

        return statsMapper.toAccommodationStatsDTO(averageRating, totalComments, totalReservations, occupancyRate, cancellations, totalRevenue);
    }

    //Corregido (pendiente), se debe crear una consulta que dado el id del host, se traiga sus alojamientos.
    @Override
    public List<AccommodationDTO> listAllAccommodationsHost(String id, int page) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Accommodation> accommodations = accommodationRepository.getAccommodations(id, pageable);

        return accommodations.toList().stream().map(showAccommodationMapper::toAccommodationDTO).collect(Collectors.toList());
    }
}

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
import co.edu.uniquindio.application.mappers.*;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.*;
import co.edu.uniquindio.application.services.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.application.services.GeoUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final AccommodationDetailMapper accommodationDetailMapper;
    private final FavoriteRepository favoriteRepository;
    private final GeoUtilsImpl geoUtilsImpl;


    @Override
    public void create(String id, CreateAccommodationDTO createAccommodationDTO) throws Exception {

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("usuario no encontrado");
        }

       if(verifyExistence(createAccommodationDTO)){
           throw new ValueConflictException("este alojamiento ya existe");
       }
       Accommodation accommodation = accommodationMapper.toEntity(createAccommodationDTO);
       accommodation.setUser(optionalUser.get());
       accommodationRepository.save(accommodation);

    }

    private boolean verifyExistence(CreateAccommodationDTO createAccommodationDTO) {

        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();

        for (Accommodation accommodation : accommodationRepository.findByState(State.ACTIVE)) {
            double distancia = geoUtilsImpl.calcularDistancia(
                    createAccommodationDTO.latitude(), createAccommodationDTO.longitude(),
                    accommodation.getLocation().getCoordinates().getLatitude(),
                    accommodation.getLocation().getCoordinates().getLongitude()
            );

            double score = similarity.apply(
                    accommodation.getTitle().toLowerCase().trim(),
                    createAccommodationDTO.title().toLowerCase().trim()
            );

            // Umbral configurable (0.85 - 85% de similitud)
            if (distancia <= 5 && score >= 0.85) {
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
    @Transactional
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

      // hacemos un soft delete, borramos los alojamientos de las listas de favoritos de la gente y guardamos
      accommodation.get().setState(State.INACTIVE);
      favoriteRepository.deleteAllByAccommodationId(accommodation.get().getId());
      accommodationRepository.save(accommodation.get());
    }



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

    @Override
    public List<Amenities> listAllAmenicties(String id) throws Exception {
        return List.of();
    }


    // devuelve la lista de todos los servicios del alojamiento
    //@Override
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
        double occupancy = bookingRepository.findAverageOccupancyByAccommodationId(id, statsDateDTO.startDate(), statsDateDTO.endDate());
        long totalDays = (statsDateDTO.startDate() != null && statsDateDTO.endDate() != null)
                ? ChronoUnit.DAYS.between(statsDateDTO.startDate(), statsDateDTO.endDate())
                : 30; // se calcula el promedio de 30 dias si el usuario no pasa fechas

        double occupancyRate = totalDays > 0 ? (occupancy / totalDays) * 100 : 0.0;
        int cancellations = bookingRepository.countCancellationsByAccommodationId(id, statsDateDTO.startDate(), statsDateDTO.endDate());
        double totalRevenue = bookingRepository.findAverageRevenueByAccommodationId(id, statsDateDTO.startDate(), statsDateDTO.endDate());

        return statsMapper.toAccommodationStatsDTO(averageRating, totalComments, totalReservations, occupancyRate, cancellations, totalRevenue);
    }

    //Corregido (pendiente), se debe crear una consulta que dado el id del host, se traiga sus alojamientos.
    @Override
    public List<AccommodationDTO> listAllAccommodationsHost(String id, int page) throws Exception {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Accommodation> accommodations = accommodationRepository.getAccommodations(id, pageable);

        return accommodations.toList().stream().map(showAccommodationMapper::toAccommodationDTO).collect(Collectors.toList());
    }

    // para ver el alojamiento
    @Override
    public AccommodationDetailDTO get(String id) throws Exception {
       Optional<Accommodation> accommodation = accommodationRepository.findById(id);
       if(accommodation.isEmpty()){
           throw new ResourceNotFoundException("no se encontró el alojamiento");
       }

        return accommodationDetailMapper.toAccommodationDetailDTO(accommodation.get());
    }
}

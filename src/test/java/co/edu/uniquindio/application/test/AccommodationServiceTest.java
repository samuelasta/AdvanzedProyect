package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDetailDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationStatsDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.CreateAccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.ListAccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.StatsDateDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.UpdateDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UserDetailDTO;
import co.edu.uniquindio.application.exceptions.BadRequestException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.AccommodationDetailMapper;
import co.edu.uniquindio.application.mappers.AccommodationMapper;
import co.edu.uniquindio.application.mappers.ShowAccommodationMapper;
import co.edu.uniquindio.application.mappers.StatsMapper;
import co.edu.uniquindio.application.model.*;
import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.repositories.CommentRepository;
import co.edu.uniquindio.application.repositories.FavoriteRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.impl.AccommodationServiceImpl;
import co.edu.uniquindio.application.services.impl.GeoUtilsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

    @Mock private AccommodationMapper accommodationMapper;
    @Mock private ShowAccommodationMapper showAccommodationMapper;
    @Mock private AccommodationRepository accommodationRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private StatsMapper statsMapper;
    @Mock private AccommodationDetailMapper accommodationDetailMapper;
    @Mock private FavoriteRepository favoriteRepository;
    @Mock private GeoUtilsImpl geoUtilsImpl;

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    private User user;
    private Accommodation accommodation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");

        accommodation = new Accommodation();
        accommodation.setId("acc123");
        accommodation.setTitle("Casa Bonita");
        accommodation.setState(State.ACTIVE);
    }


    @Test
    void create_WhenUserExistsAndAccommodationNotDuplicate_ShouldSave() throws Exception {
        CreateAccommodationDTO dto = new CreateAccommodationDTO(
                "Casa Bonita", "Hermosa casa con vista y patio amplio",
                120000, List.of("img1.png"),
                AccommodationType.HOUSE, 4,
                "Colombia", "Quindio", "Armenia",
                "La Castellana", "Calle 10", "630004",
                List.of(Amenities.WIFI), 4.533f, -75.681f
        );

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(accommodationRepository.findByState(State.ACTIVE)).thenReturn(List.of());
        when(accommodationMapper.toEntity(dto)).thenReturn(accommodation);

        accommodationService.create("user123", dto);

        verify(accommodationRepository).save(accommodation);
    }

    @Test
    void create_WhenUserNotFound_ShouldThrowException() {
        CreateAccommodationDTO dto = new CreateAccommodationDTO(
                "Casa Bonita", "Hermosa casa con vista y patio amplio",
                120000, List.of("img1.png"),
                AccommodationType.HOUSE, 4,
                "Colombia", "Quindio", "Armenia",
                "La Castellana", "Calle 10", "630004",
                List.of(Amenities.WIFI), 4.533f, -75.681f
        );

        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accommodationService.create("user123", dto));
    }

    @Test
    void create_WhenAccommodationAlreadyExists_ShouldThrowConflict() {
        CreateAccommodationDTO dto = new CreateAccommodationDTO(
                "Casa Bonita", "Hermosa casa con vista y patio amplio",
                120000, List.of("img1.png"),
                AccommodationType.HOUSE, 4,
                "Colombia", "Quindio", "Armenia",
                "La Castellana", "Calle 10", "630004",
                List.of(Amenities.WIFI), 4.533f, -75.681f
        );

        Accommodation existing = new Accommodation();
        existing.setTitle("Casa Bonita");

        Location location = new Location();
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(4.533f);
        coordinates.setLongitude(75.681f);
        location.setCoordinates(coordinates);
        existing.setLocation(location);

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(accommodationRepository.findByState(State.ACTIVE)).thenReturn(List.of(existing));

        // distancia pequeña
        when(geoUtilsImpl.calcularDistancia(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(1.0);

        // Ejecuta y verifica excepción esperada
        assertThrows(ValueConflictException.class, () -> accommodationService.create("user123", dto));
    }


    @Test
    void update_WhenAccommodationExists_ShouldSave() throws Exception {
        UpdateDTO updateDTO = new UpdateDTO(
                "Titulo Nuevo",
                "Descripción suficientemente larga para validar",
                5,
                150000.0,
                "Colombia",
                "Quindio",
                "Armenia",
                "La Castellana",
                "Calle 10",
                "630004",
                List.of("pic1.png", "pic2.png"),
                List.of(Amenities.WIFI, Amenities.BATHROOM),
                AccommodationType.APARTMENT
        );

        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));

        accommodationService.update("acc123", updateDTO);

        verify(accommodationMapper).updateAccommodationFromDto(updateDTO, accommodation);
        verify(accommodationRepository).save(accommodation);
    }

    @Test
    void update_WhenAccommodationNotFound_ShouldThrow() {
        UpdateDTO updateDTO = new UpdateDTO(
                "Titulo Nuevo",
                "Descripción suficientemente larga para validar",
                5,
                150000.0,
                "Colombia",
                "Quindio",
                "Armenia",
                "La Castellana",
                "Calle 10",
                "630004",
                List.of("pic1.png"),
                List.of(Amenities.WIFI),
                AccommodationType.HOUSE
        );

        when(accommodationRepository.findById("acc123")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accommodationService.update("acc123", updateDTO));
    }


    @Test
    void delete_WhenValidAndNoPendingBooking_ShouldSetInactive() throws Exception {
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));
        when(bookingRepository.findByAccommodationIdAndBookingState("acc123", BookingState.PENDING))
                .thenReturn(Optional.empty());

        accommodationService.delete("acc123");

        assertEquals(State.INACTIVE, accommodation.getState());
        verify(favoriteRepository).deleteAllByAccommodationId("acc123");
        verify(accommodationRepository).save(accommodation);
    }

    @Test
    void delete_WhenAccommodationNotFound_ShouldThrow() {
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accommodationService.delete("acc123"));
    }

    @Test
    void delete_WhenHasPendingBooking_ShouldThrowUnauthorized() {
        Booking booking = new Booking();
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));
        when(bookingRepository.findByAccommodationIdAndBookingState("acc123", BookingState.PENDING))
                .thenReturn(Optional.of(booking));

        assertThrows(UnauthorizedException.class, () -> accommodationService.delete("acc123"));
    }

    @Test
    void search_WhenValid_ShouldReturnList() throws Exception {
        ListAccommodationDTO filters = new ListAccommodationDTO(
                "Armenia", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                2, 100.0, 300.0, List.of(Amenities.WIFI)
        );

        Accommodation acc = new Accommodation();
        Page<Accommodation> page = new PageImpl<>(List.of(acc));
        when(accommodationRepository.searchAccommodations(eq(filters), any(Pageable.class))).thenReturn(page);
        when(showAccommodationMapper.toAccommodationDTO(acc))
                .thenReturn(new AccommodationDTO("Casa Bonita", 120000, "foto.png", 4.5, "Armenia"));

        List<AccommodationDTO> result = accommodationService.search(filters, 0);

        assertEquals(1, result.size());
    }

    @Test
    void search_WhenMinPriceGreaterThanMax_ShouldThrowBadRequest() {
        ListAccommodationDTO filters = new ListAccommodationDTO(
                "Armenia", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                2, 300.0, 100.0, List.of(Amenities.WIFI)
        );
        assertThrows(BadRequestException.class, () -> accommodationService.search(filters, 0));
    }

    @Test
    void search_WhenNoResults_ShouldThrowNotFound() {
        ListAccommodationDTO filters = new ListAccommodationDTO(
                "Armenia", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                2, 100.0, 300.0, List.of(Amenities.WIFI)
        );
        Page<Accommodation> emptyPage = new PageImpl<>(List.of());
        when(accommodationRepository.searchAccommodations(eq(filters), any(Pageable.class)))
                .thenReturn(emptyPage);

        assertThrows(ResourceNotFoundException.class, () -> accommodationService.search(filters, 0));
    }


    @Test
    void listAllAmenities_WhenAccommodationExists_ShouldReturnAmenities() throws Exception {
        accommodation.setAmenities(List.of(Amenities.WIFI, Amenities.BATHROOM));
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));

        var result = accommodationService.listAllAmenities("acc123");

        assertEquals(2, result.size());
    }

    @Test
    void listAllAmenities_WhenNotFound_ShouldThrow() {
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accommodationService.listAllAmenities("acc123"));
    }



    @Test
    void stats_WhenValid_ShouldReturnStatsDTO() throws Exception {
        StatsDateDTO dto = new StatsDateDTO(LocalDateTime.now().minusDays(30), LocalDateTime.now());
        when(commentRepository.findAverageRatingByAccommodationId(anyString(), any(), any())).thenReturn(4.5);
        when(commentRepository.countByAccommodationId(anyString(), any(), any())).thenReturn(10L);
        when(bookingRepository.countByAccommodationIdAndBetween(anyString(), any(), any())).thenReturn(5L);
        when(bookingRepository.findAverageOccupancyByAccommodationId(anyString(), any(), any())).thenReturn(15.0);
        when(bookingRepository.countCancellationsByAccommodationId(anyString(), any(), any())).thenReturn(1);
        when(bookingRepository.findAverageRevenueByAccommodationId(anyString(), any(), any())).thenReturn(250000.0);

        AccommodationStatsDTO stats = new AccommodationStatsDTO(4.5, 10L, 5L, 50.0, 1, 250000.0);
        when(statsMapper.toAccommodationStatsDTO(anyDouble(), anyLong(), anyLong(), anyDouble(), anyInt(), anyDouble()))
                .thenReturn(stats);

        AccommodationStatsDTO result = accommodationService.stats("acc123", dto);

        assertEquals(4.5, result.averageRating());
    }


    @Test
    void listAllAccommodationsHost_WhenValid_ShouldReturnList() throws Exception {
        Page<Accommodation> page = new PageImpl<>(List.of(accommodation));
        when(accommodationRepository.getAccommodations(eq("user123"), any(Pageable.class)))
                .thenReturn(page);
        when(showAccommodationMapper.toAccommodationDTO(accommodation))
                .thenReturn(new AccommodationDTO("Casa Bonita", 120000, "foto.png", 4.5, "Armenia"));

        List<AccommodationDTO> result = accommodationService.listAllAccommodationsHost("user123", 0);

        assertEquals(1, result.size());
    }



    @Test
    void get_WhenAccommodationExists_ShouldReturnDetail() throws Exception {
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));

        UserDetailDTO userDetailDTO = new UserDetailDTO("user123", "Ana", "ana@mail.com", LocalDateTime.now());
        AccommodationDetailDTO dto = new AccommodationDetailDTO(
                "acc123",
                4.533f,
                -75.681f,
                120000.0,
                List.of("foto1.png", "foto2.png"),
                "Descripción amplia y válida",
                List.of(Amenities.WIFI, Amenities.ENTERTAINMENT),
                "Casa Bonita",
                4,
                4.6,
                userDetailDTO
        );

        when(accommodationDetailMapper.toAccommodationDetailDTO(accommodation)).thenReturn(dto);

        AccommodationDetailDTO result = accommodationService.get("acc123");

        assertEquals("acc123", result.id());
        assertEquals("Casa Bonita", result.title());
        assertEquals(120000.0, result.price());
    }

    @Test
    void get_WhenNotFound_ShouldThrow() {
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accommodationService.get("acc123"));
    }
}

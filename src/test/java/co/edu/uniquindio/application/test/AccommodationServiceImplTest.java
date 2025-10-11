package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.accommodationDTO.*;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.AccommodationMapper;
import co.edu.uniquindio.application.mappers.ShowAccommodationMapper;
import co.edu.uniquindio.application.mappers.StatsMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Coordinates;
import co.edu.uniquindio.application.model.Location;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.repositories.CommentRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.impl.AccommodationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {

    @Mock AccommodationMapper accommodationMapper;
    @Mock ShowAccommodationMapper showAccommodationMapper;
    @Mock AccommodationRepository accommodationRepository;
    @Mock BookingRepository bookingRepository;
    @Mock UserRepository userRepository;
    @Mock CommentRepository commentRepository;
    @Mock StatsMapper statsMapper;

    @InjectMocks AccommodationServiceImpl service;

    // ----------------- helpers -----------------

    private CreateAccommodationDTO makeCreateDTO(float lat, float lon, String title) {
        return new CreateAccommodationDTO(
                title,                              // title (5..25 chars recomendado por validaciones)
                "Descripción suficientemente larga para pasar validación.", // description
                150_000,                            // price
                List.of("https://img/1.jpg"),       // picsUrl
                AccommodationType.APARTMENT,        // accommodationType
                4,                                  // capacity
                "Colombia",                         // country
                "Valle",                            // department
                "Cali",                             // city
                "San Antonio",                      // neighborhood
                "Cra 1 # 2-3",                      // street
                "A1B2C3",                           // postalCode (patrón 4..10 alfanumérico)
                List.copyOf(EnumSet.of(Amenities.WIFI, Amenities.PARKING_AND_FACILITIES)), // amenities >=1
                lat, lon                            // latitude, longitude
        );
    }

    private Accommodation makeActiveAccommodationAt(float lat, float lon, String title) {
        Coordinates coords = new Coordinates();
        coords.setLatitude(lat);
        coords.setLongitude(lon);
        Location loc = new Location();
        loc.setCoordinates(coords);

        Accommodation a = new Accommodation();
        a.setTitle(title);
        a.setLocation(loc);
        a.setState(State.ACTIVE);
        return a;
    }

    // ----------------- create() -----------------

    @Test
    @DisplayName("create(): usuario inexistente -> ResourceNotFound")
    void create_UserNotFound() {
        when(userRepository.findById("u1")).thenReturn(Optional.empty());

        var dto = makeCreateDTO(4.0f, -76.0f, "Casa Centro");
        assertThrows(ResourceNotFoundException.class, () -> service.create("u1", dto));
    }

    @Test
    @DisplayName("create(): duplicado por cercanía (<=5) y mismo título -> ValueConflict")
    void create_DuplicateByProximityAndTitle() throws Exception {
        when(userRepository.findById("u1")).thenReturn(Optional.of(new User()));
        // Alojamiento activo existente en mismas coords y mismo título -> distancia 0
        when(accommodationRepository.findByState(State.ACTIVE))
                .thenReturn(List.of(makeActiveAccommodationAt(4.0f, -76.0f, "Casa Centro")));

        var dto = makeCreateDTO(4.0f, -76.0f, "Casa Centro");
        assertThrows(ValueConflictException.class, () -> service.create("u1", dto));
    }

    @Test
    @DisplayName("create(): ok -> mapea, asigna user y guarda")
    void create_Success() throws Exception {
        User u = new User(); u.setId("u1");
        when(userRepository.findById("u1")).thenReturn(Optional.of(u));
        when(accommodationRepository.findByState(State.ACTIVE)).thenReturn(List.of()); // no dup

        Accommodation mapped = new Accommodation();
        when(accommodationMapper.toEntity(any(CreateAccommodationDTO.class))).thenReturn(mapped);

        var dto = makeCreateDTO(4.1f, -76.1f, "Casa Norte");
        service.create("u1", dto);

        ArgumentCaptor<Accommodation> cap = ArgumentCaptor.forClass(Accommodation.class);
        verify(accommodationRepository).save(cap.capture());
        assertSame(u, cap.getValue().getUser());
    }

    // ----------------- update() -----------------

    @Test
    @DisplayName("update(): alojamiento inexistente -> ResourceNotFound")
    void update_NotFound() {
        when(accommodationRepository.findById("a1")).thenReturn(Optional.empty());
        var update = new UpdateDTO(
                "Titulo Nuevo", "Descripción nueva suficiente", 4, 200_000.0,
                "Colombia", "Valle", "Cali", "San Antonio", "Cra 1 # 2-3",
                "Z9X8Y7", List.of("https://img/2.jpg"),
                List.copyOf(EnumSet.of(Amenities.WIFI)), AccommodationType.HOUSE
        );
        assertThrows(ResourceNotFoundException.class, () -> service.update("a1", update));
    }

    @Test
    @DisplayName("update(): ok -> aplica mapper y guarda")
    void update_Success() throws Exception {
        Accommodation acc = new Accommodation(); acc.setId("a1");
        when(accommodationRepository.findById("a1")).thenReturn(Optional.of(acc));

        var update = new UpdateDTO(
                "Titulo Nuevo", "Descripción nueva suficiente", 4, 200_000.0,
                "Colombia", "Valle", "Cali", "San Antonio", "Cra 1 # 2-3",
                "Z9X8Y7", List.of("https://img/2.jpg"),
                List.copyOf(EnumSet.of(Amenities.WIFI)), AccommodationType.HOUSE
        );

        service.update("a1", update);

        verify(accommodationMapper).updateAccommodationFromDto(eq(update), eq(acc));
        verify(accommodationRepository).save(acc);
    }

    // ----------------- delete() -----------------

    @Test
    @DisplayName("delete(): alojamiento inexistente -> ResourceNotFound")
    void delete_NotFound() {
        when(accommodationRepository.findById("a1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete("a1"));
    }

    @Test
    @DisplayName("delete(): tiene reservas PENDING -> Unauthorized")
    void delete_WithPendingBooking() {
        Accommodation acc = new Accommodation(); acc.setId("a1");
        when(accommodationRepository.findById("a1")).thenReturn(Optional.of(acc));
        when(bookingRepository.findByAccommodationIdAndBookingState("a1", BookingState.PENDING))
                .thenReturn(Optional.of(new co.edu.uniquindio.application.model.Booking()));

        assertThrows(UnauthorizedException.class, () -> service.delete("a1"));
    }

    @Test
    @DisplayName("delete(): sin PENDING -> pone INACTIVE y guarda")
    void delete_SetsInactiveAndSaves() throws Exception {
        Accommodation acc = new Accommodation(); acc.setId("a1"); acc.setState(State.ACTIVE);
        when(accommodationRepository.findById("a1")).thenReturn(Optional.of(acc));
        when(bookingRepository.findByAccommodationIdAndBookingState("a1", BookingState.PENDING))
                .thenReturn(Optional.empty());

        service.delete("a1");

        assertEquals(State.INACTIVE, acc.getState());
        verify(accommodationRepository).save(acc);
    }

    // ----------------- search() -----------------

    @Test
    @DisplayName("search(): sin resultados -> ResourceNotFound")
    void search_Empty() {
        ListAccommodationDTO filters = new ListAccommodationDTO(
                "Cali",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                2
        );
        when(accommodationRepository.searchAccommodations(eq(filters), any(Pageable.class)))
                .thenReturn(Page.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.search(filters, 0));
    }

    @Test
    @DisplayName("search(): ok -> mapea y devuelve lista")
    void search_ReturnsMappedList() throws Exception {
        ListAccommodationDTO filters = new ListAccommodationDTO(
                "Cali",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                2
        );

        Accommodation acc = new Accommodation();
        when(accommodationRepository.searchAccommodations(eq(filters), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(acc)));

        when(showAccommodationMapper.toAccommodationDTO(acc))
                .thenReturn(new AccommodationDTO("Casa Centro", 150_000, "https://img/1.jpg", 4.6, "Cali"));

        var result = service.search(filters, 0);

        assertEquals(1, result.size());
        assertEquals("Casa Centro", result.get(0).title());
        verify(showAccommodationMapper, times(1)).toAccommodationDTO(acc);
    }

    // ----------------- listAllAmenities() (método público en la impl) -----------------

    @Test
    @DisplayName("listAllAmenities(): alojamiento inexistente -> ResourceNotFound")
    void listAllAmenities_NotFound() {
        when(accommodationRepository.findById("a1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.listAllAmenities("a1"));
    }

    @Test
    @DisplayName("listAllAmenities(): ok -> retorna amenities del alojamiento")
    void listAllAmenities_ReturnsAmenities() throws Exception {
        Accommodation acc = new Accommodation(); acc.setId("a1");
        List<Amenities> amenities = new ArrayList<>();
        amenities.add(Amenities.WIFI);
        amenities.add(Amenities.PARKING_AND_FACILITIES);
        acc.setAmenities(amenities);

        when(accommodationRepository.findById("a1")).thenReturn(Optional.of(acc));

        var res = service.listAllAmenities("a1");
        assertEquals(2, res.size());
        assertTrue(res.containsAll(amenities));
    }

    // ----------------- stats() -----------------

    @Test
    @DisplayName("stats(): agrega métricas desde repos y delega en mapper")
    void stats_ReturnsFromMapper() throws Exception {
        String accId = "a1";
        StatsDateDTO range = new StatsDateDTO(null, null);

        // Stubs de agregaciones que usa el servicio
        when(commentRepository.findAverageRatingByAccommodationId(eq(accId), any(), any())).thenReturn(4.2);
        when(commentRepository.countByAccommodationId(eq(accId), any(), any())).thenReturn(7L);
        when(bookingRepository.countByAccommodationIdAndBetween(eq(accId), any(), any())).thenReturn(12L);
        when(bookingRepository.findAverageOccupancyByAccommodationId(eq(accId), any(), any())).thenReturn(0.65);
        when(bookingRepository.countCancellationsByAccommodationId(eq(accId), eq(BookingState.CANCELED), any(), any())).thenReturn(2);
        when(bookingRepository.findAverageRevenueByAccommodationId(eq(accId), eq(BookingState.COMPLETED), any(), any())).thenReturn(350.0);

        // El mapper construye AccommodationStatsDTO (8 campos). El servicio le pasa 6 valores; el mapper puede setear los otros 2.
        var dto = new AccommodationStatsDTO(
                4.2, 7L, 12L, 0.65, 2,
                null, null, // lastReservation, nextAvailableDate
                350.0
        );

        when(statsMapper.toAccommodationStatsDTO(
                eq(4.2), eq(7L), eq(12L), eq(0.65), eq(2), eq(350.0)
        )).thenReturn(dto);

        var res = service.stats(accId, range);

        assertNotNull(res);
        assertEquals(4.2, res.averageRating());
        assertEquals(7L, res.totalComments());
        assertEquals(12L, res.totalReservations());
        assertEquals(0.65, res.occupancyRate());
        assertEquals(2, res.cancellations());
        assertEquals(350.0, res.totalRevenue());

        verify(statsMapper).toAccommodationStatsDTO(4.2, 7L, 12L, 0.65, 2, 350.0);
    }

    // ----------------- listAllAccommodationsHost() -----------------

    @Test
    @DisplayName("listAllAccommodationsHost(): mapea el page a DTOs")
    void listAllAccommodationsHost_Maps() throws Exception {
        Accommodation a1 = new Accommodation();
        Accommodation a2 = new Accommodation();

        when(accommodationRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(a1, a2)));

        when(showAccommodationMapper.toAccommodationDTO(a1))
                .thenReturn(new AccommodationDTO("A1", 100_000, "ph1", 4.0, "Cali"));
        when(showAccommodationMapper.toAccommodationDTO(a2))
                .thenReturn(new AccommodationDTO("A2", 120_000, "ph2", 4.5, "Cali"));

        var list = service.listAllAccommodationsHost("host-1");

        assertEquals(2, list.size());
        assertEquals("A1", list.get(0).title());
        assertEquals("A2", list.get(1).title());
        verify(accommodationRepository).findAll(any(Pageable.class));
        verify(showAccommodationMapper, times(1)).toAccommodationDTO(a1);
        verify(showAccommodationMapper, times(1)).toAccommodationDTO(a2);
    }
}

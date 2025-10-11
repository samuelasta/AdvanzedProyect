package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.bookingDTO.BookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.dto.bookingDTO.SearchBookingDTO;
import co.edu.uniquindio.application.exceptions.*;
import co.edu.uniquindio.application.mappers.BookingMapper;
import co.edu.uniquindio.application.model.*;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.repositories.*;
import co.edu.uniquindio.application.services.impl.BookingServiceImpl;
import co.edu.uniquindio.application.services.impl.CurrentUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock private BookingMapper bookingMapper;
    @Mock private BookingRepository bookingRepository;
    @Mock private AccommodationRepository accommodationRepository;
    @Mock private UserRepository userRepository;
    @Mock private CurrentUserServiceImpl currentUserService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Accommodation accommodation;
    private User user;
    private Booking booking;

    @BeforeEach
    void setUp() {
        accommodation = new Accommodation();
        accommodation.setId("acc1");

        user = new User();
        user.setId("user1");

        booking = new Booking();
        booking.setId("b1");
        booking.setUser(user);
        booking.setAccommodation(accommodation);
        booking.setBookingState(BookingState.PENDING);
    }

    // ---------------- CREATE ----------------

    @Test
    void create_WhenValid_ShouldSaveBooking() throws Exception {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(2);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(4);
        CreateBookingDTO dto = new CreateBookingDTO(checkIn, checkOut, 2);

        when(bookingRepository.existsOverlappingBooking(anyString(), any(), any())).thenReturn(false);
        when(accommodationRepository.findById("acc1")).thenReturn(Optional.of(accommodation));
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));

        Booking mockBooking = new Booking();
        when(bookingMapper.toEntity(dto, accommodation, user)).thenReturn(mockBooking);

        bookingService.create("acc1", "user1", dto);

        verify(bookingRepository).save(mockBooking);
    }

    @Test
    void create_WhenCheckInInPast_ShouldThrowBadRequest() {
        CreateBookingDTO dto = new CreateBookingDTO(LocalDateTime.now().minusDays(1),
                LocalDateTime.now(), 2);
        assertThrows(BadRequestException.class, () -> bookingService.create("acc1", "user1", dto));
    }

    @Test
    void create_WhenCheckInAfterCheckOut_ShouldThrowBadRequest() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(5);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(3);
        CreateBookingDTO dto = new CreateBookingDTO(checkIn, checkOut, 2);
        assertThrows(BadRequestException.class, () -> bookingService.create("acc1", "user1", dto));
    }

    @Test
    void create_WhenDatesOverlap_ShouldThrowValueConflict() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(2);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(5);
        CreateBookingDTO dto = new CreateBookingDTO(checkIn, checkOut, 2);
        when(bookingRepository.existsOverlappingBooking(anyString(), any(), any())).thenReturn(true);

        assertThrows(ValueConflictException.class, () -> bookingService.create("acc1", "user1", dto));
    }

    @Test
    void create_WhenAccommodationNotFound_ShouldThrowNotFound() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(2);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(5);
        CreateBookingDTO dto = new CreateBookingDTO(checkIn, checkOut, 2);
        when(bookingRepository.existsOverlappingBooking(anyString(), any(), any())).thenReturn(false);
        when(accommodationRepository.findById("acc1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.create("acc1", "user1", dto));
    }

    @Test
    void create_WhenUserNotFound_ShouldThrowNotFound() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(2);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(5);
        CreateBookingDTO dto = new CreateBookingDTO(checkIn, checkOut, 2);
        when(bookingRepository.existsOverlappingBooking(anyString(), any(), any())).thenReturn(false);
        when(accommodationRepository.findById("acc1")).thenReturn(Optional.of(accommodation));
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.create("acc1", "user1", dto));
    }

    // ---------------- DELETE ----------------

    @Test
    void delete_WhenValidPendingAndBefore48Hours_ShouldCancelBooking() throws Exception {
        booking.setBookingState(BookingState.PENDING);
        booking.setCheckIn(LocalDateTime.now().plusDays(3));

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(currentUserService.getCurrentUser()).thenReturn("user1");

        bookingService.delete("b1");

        assertEquals(BookingState.CANCELED, booking.getBookingState());
        verify(bookingRepository).save(booking);
    }

    @Test
    void delete_WhenBookingNotFound_ShouldThrowNotFound() {
        when(bookingRepository.findById("b1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookingService.delete("b1"));
    }

    @Test
    void delete_WhenUserDoesNotOwnBooking_ShouldThrowForbidden() {
        booking.setUser(new User());
        booking.getUser().setId("otherUser");

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(currentUserService.getCurrentUser()).thenReturn("user1");

        assertThrows(ForbiddenException.class, () -> bookingService.delete("b1"));
    }

    @Test
    void delete_WhenWithin48Hours_ShouldThrowValueConflict() {
        booking.setCheckIn(LocalDateTime.now().plusHours(24));
        booking.setBookingState(BookingState.PENDING);

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(currentUserService.getCurrentUser()).thenReturn("user1");

        assertThrows(ValueConflictException.class, () -> bookingService.delete("b1"));
    }

    @Test
    void delete_WhenBookingNotPending_ShouldThrowUnauthorized() {
        booking.setBookingState(BookingState.COMPLETED);

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(currentUserService.getCurrentUser()).thenReturn("user1");

        assertThrows(UnauthorizedException.class, () -> bookingService.delete("b1"));
    }

    // ---------------- LIST BOOKINGS ----------------

    @Test
    void listBookings_WhenAccommodationValid_ShouldReturnList() throws Exception {
        Accommodation acc = new Accommodation();
        acc.setId("acc1");

        when(accommodationRepository.findById("acc1")).thenReturn(Optional.of(acc));
        Booking b = new Booking();
        Page<Booking> page = new PageImpl<>(List.of(b));

        when(bookingRepository.findBookingsByAccommodationWithFilters(eq("acc1"), any(), any(Pageable.class)))
                .thenReturn(page);
        when(bookingMapper.toBookingDTO(b)).thenReturn(new BookingDTO(BookingState.PENDING, null,
                LocalDate.now(), LocalDate.now().plusDays(2), 2));

        List<BookingDTO> result = bookingService.listBookings("acc1", 0,
                new SearchBookingDTO(null, null, null, null));

        assertEquals(1, result.size());
    }

    @Test
    void listBookings_WhenAccommodationNotFound_ShouldThrow() {
        when(accommodationRepository.findById("acc1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.listBookings("acc1", 0, new SearchBookingDTO(null, null, null, null)));
    }

    @Test
    void listBookings_WhenGuestNumberInvalid_ShouldThrowBadRequest() {
        SearchBookingDTO dto = new SearchBookingDTO(null, null, null, 0);
        assertThrows(BadRequestException.class,
                () -> bookingService.listBookings("acc1", 0, dto));
    }

    // ---------------- LIST BOOKINGS USER ----------------

    @Test
    void listBookingsUser_WhenUserValid_ShouldReturnList() throws Exception {
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        Booking b = new Booking();
        Page<Booking> page = new PageImpl<>(List.of(b));

        when(bookingRepository.findBookingsByUserWithFilters(eq("user1"), any(), any(Pageable.class)))
                .thenReturn(page);
        when(bookingMapper.toBookingDTO(b)).thenReturn(new BookingDTO(BookingState.PENDING, null,
                LocalDate.now(), LocalDate.now().plusDays(2), 2));

        List<BookingDTO> result = bookingService.listBookingsUser("user1", 0,
                new SearchBookingDTO(null, null, null, null));

        assertEquals(1, result.size());
    }

    @Test
    void listBookingsUser_WhenUserNotFound_ShouldThrow() {
        when(userRepository.findById("user1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.listBookingsUser("user1", 0, new SearchBookingDTO(null, null, null, null)));
    }
}

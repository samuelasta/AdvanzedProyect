package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.commentDTO.CommentDTO;
import co.edu.uniquindio.application.dto.commentDTO.CreateCommentDTO;
import co.edu.uniquindio.application.exceptions.ForbiddenException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.mappers.CommentMapper;
import co.edu.uniquindio.application.mappers.ListCommentsMapper;
import co.edu.uniquindio.application.model.*;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.repositories.*;
import co.edu.uniquindio.application.services.impl.CommentServiceImpl;
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
class CommentServiceImplTest {

    @Mock private CommentRepository commentRepository;
    @Mock private AccommodationRepository accommodationRepository;
    @Mock private ListCommentsMapper listCommentsMapper;
    @Mock private CommentMapper commentMapper;
    @Mock private UserRepository userRepository;
    @Mock private BookingRepository bookingRepository;

    @InjectMocks
    private CommentServiceImpl service;

    private Booking booking;
    private User user;
    private Accommodation accommodation;

    @BeforeEach
    void setUp() {
        accommodation = new Accommodation();
        accommodation.setId("acc-1");
        accommodation.setAverageRatings(4.0);

        user = new User();
        user.setId("user-1");

        booking = new Booking();
        booking.setId("b1");
        booking.setUser(user);
        booking.setAccommodation(accommodation);
        booking.setBookingState(BookingState.COMPLETED);
    }

    // ---------------- LIST COMMENTS ----------------

    @Test
    void listComments_WhenHasResults_ShouldReturnList() throws Exception {
        Comment comment = new Comment();
        Page<Comment> page = new PageImpl<>(List.of(comment));
        when(commentRepository.findAllByAccommodationId(eq("acc-1"), any(Pageable.class))).thenReturn(page);
        when(listCommentsMapper.ToCommentDTO(comment))
                .thenReturn(new CommentDTO(
                        "Excelente",
                        LocalDateTime.now(),
                        5,
                        null // o construye un UserCommentDTO si lo necesitas
                ));


        List<CommentDTO> result = service.listComments("acc-1", 0);

        assertEquals(1, result.size());
        verify(commentRepository).findAllByAccommodationId(eq("acc-1"), any(Pageable.class));
    }

    @Test
    void listComments_WhenEmpty_ShouldThrowNotFound() {
        Page<Comment> empty = new PageImpl<>(List.of());
        when(commentRepository.findAllByAccommodationId(eq("acc-1"), any(Pageable.class))).thenReturn(empty);

        assertThrows(ResourceNotFoundException.class, () -> service.listComments("acc-1", 0));
    }

    // ---------------- CREATE COMMENT ----------------

    @Test
    void createComment_WhenValid_ShouldSaveAndUpdateAccommodation() throws Exception {
        var dto = new CreateCommentDTO("Excelente", 5);

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(commentRepository.existsByBookingId("b1")).thenReturn(false);
        when(commentMapper.toEntity(dto)).thenReturn(new Comment());
        when(commentRepository.findAverageRatingByAccommodationId(eq("acc-1"), any(), any())).thenReturn(4.8);

        service.createComment("b1", "user-1", dto);

        verify(commentRepository).save(any(Comment.class));
        verify(accommodationRepository).save(accommodation);
    }

    @Test
    void createComment_WhenBookingNotFound_ShouldThrowNotFound() {
        var dto = new CreateCommentDTO("Excelente", 5);
        when(bookingRepository.findById("b1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createComment("b1", "user-1", dto));
    }

    @Test
    void createComment_WhenBookingNotCompleted_ShouldThrowForbidden() {
        var dto = new CreateCommentDTO("Excelente", 5);
        booking.setBookingState(BookingState.PENDING);
        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));

        assertThrows(ForbiddenException.class, () -> service.createComment("b1", "user-1", dto));
    }

    @Test
    void createComment_WhenUserNotFound_ShouldThrowNotFound() {
        var dto = new CreateCommentDTO("Excelente", 5);
        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(userRepository.findById("user-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createComment("b1", "user-1", dto));
    }

    @Test
    void createComment_WhenUserNotOwner_ShouldThrowForbidden() {
        var dto = new CreateCommentDTO("Excelente", 5);
        User otherUser = new User();
        otherUser.setId("different");
        booking.setUser(otherUser);

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        assertThrows(ForbiddenException.class, () -> service.createComment("b1", "user-1", dto));
    }

    @Test
    void createComment_WhenAlreadyExists_ShouldThrowForbidden() {
        var dto = new CreateCommentDTO("Excelente", 5);
        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(commentRepository.existsByBookingId("b1")).thenReturn(true);

        assertThrows(ForbiddenException.class, () -> service.createComment("b1", "user-1", dto));
    }
}

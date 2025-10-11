package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDTO;
import co.edu.uniquindio.application.exceptions.ForbiddenException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.FavoriteMapper;
import co.edu.uniquindio.application.mappers.ShowAccommodationMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Favorites;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.FavoriteRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.impl.CurrentUserServiceImpl;
import co.edu.uniquindio.application.services.impl.FavoriteServiceImpl;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock private AccommodationRepository accommodationRepository;
    @Mock private FavoriteRepository favoriteRepository;
    @Mock private UserRepository userRepository;
    @Mock private CurrentUserServiceImpl currentUserServiceImpl;
    @Mock private FavoriteMapper favoriteMapper;
    @Mock private ShowAccommodationMapper showAccommodationMapper;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private User user;
    private Accommodation accommodation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user123");

        accommodation = new Accommodation();
        accommodation.setId("acc123");
    }

    // ----------------------- ADD -----------------------

    @Test
    void add_WhenValid_ShouldSaveFavorite() throws Exception {
        // Arrange
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(favoriteRepository.existsByUserIdAndAccommodationId("user123", "acc123")).thenReturn(false);

        Favorites favorite = new Favorites();
        when(favoriteMapper.favoritesToFavorites(accommodation, user)).thenReturn(favorite);

        // Act
        favoriteService.add("acc123");

        // Assert
        verify(favoriteRepository).save(favorite);
    }

    @Test
    void add_WhenAccommodationNotFound_ShouldThrowException() {
        // Arrange
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.add("acc123"));
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void add_WhenUserNotFound_ShouldThrowForbiddenException() {
        // Arrange
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> favoriteService.add("acc123"));
    }

    @Test
    void add_WhenFavoriteAlreadyExists_ShouldThrowConflictException() {
        // Arrange
        when(accommodationRepository.findById("acc123")).thenReturn(Optional.of(accommodation));
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(favoriteRepository.existsByUserIdAndAccommodationId("user123", "acc123")).thenReturn(true);

        // Act & Assert
        assertThrows(ValueConflictException.class, () -> favoriteService.add("acc123"));
    }

    // ----------------------- DELETE -----------------------

    @Test
    void delete_WhenValid_ShouldDeleteFavorite() throws Exception {
        // Arrange
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(favoriteRepository.deleteByUserIdAndAccommodationId("user123", "acc123")).thenReturn(1);

        // Act
        favoriteService.delete("acc123");

        // Assert
        verify(favoriteRepository).deleteByUserIdAndAccommodationId("user123", "acc123");
    }

    @Test
    void delete_WhenUserNotFound_ShouldThrowForbiddenException() {
        // Arrange
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> favoriteService.delete("acc123"));
    }

    @Test
    void delete_WhenFavoriteNotFound_ShouldThrowResourceNotFound() {
        // Arrange
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(favoriteRepository.deleteByUserIdAndAccommodationId("user123", "acc123")).thenReturn(0);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.delete("acc123"));
    }

    // ----------------------- GET ALL -----------------------

    @Test
    void getAll_WhenUserHasFavorites_ShouldReturnList() throws Exception {
        // Arrange
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");

        Pageable pageable = PageRequest.of(0, 10);
        List<Accommodation> list = List.of(accommodation);
        Page<Accommodation> page = new PageImpl<>(list, pageable, list.size());

        when(favoriteRepository.findAccommodationsByUserId("user123", pageable)).thenReturn(page);
        when(showAccommodationMapper.toAccommodationDTO(accommodation))
                .thenReturn(new AccommodationDTO("Casa Bonita", 200.0, "foto.png", 4.5, "Armenia"));


        // Act
        List<AccommodationDTO> result = favoriteService.getAll(0);

        // Assert
        assertEquals(1, result.size());
        verify(favoriteRepository).findAccommodationsByUserId("user123", pageable);
    }

    @Test
    void getAll_WhenUserHasNoFavorites_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(currentUserServiceImpl.getCurrentUser()).thenReturn("user123");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Accommodation> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(favoriteRepository.findAccommodationsByUserId("user123", pageable)).thenReturn(emptyPage);

        // Act
        List<AccommodationDTO> result = favoriteService.getAll(0);

        // Assert
        assertTrue(result.isEmpty());
        verify(favoriteRepository).findAccommodationsByUserId("user123", pageable);
    }
}

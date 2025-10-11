package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.services.impl.CurrentUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    private CurrentUserServiceImpl currentUserService;

    @BeforeEach
    void setUp() {
        currentUserService = new CurrentUserServiceImpl();
    }

    @Test
    void getCurrentUser_WhenAuthenticated_ShouldReturnUsername() {
        // Arrange
        User mockUser = new User("12345", "password", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());

        SecurityContext mockContext = mock(SecurityContext.class);
        when(mockContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(mockContext);

        // Act
        String result = currentUserService.getCurrentUser();

        // Assert
        assertEquals("12345", result);
        verify(mockContext).getAuthentication();
    }

    @Test
    void getCurrentUser_WhenNoAuthentication_ShouldThrowException() {
        SecurityContext mockContext = mock(SecurityContext.class);
        when(mockContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(mockContext);

        assertThrows(NullPointerException.class, () -> currentUserService.getCurrentUser());
    }

}

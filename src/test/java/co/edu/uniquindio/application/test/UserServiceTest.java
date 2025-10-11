package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.hostDTO.HostDTO;
import co.edu.uniquindio.application.dto.usersDTOs.*;
import co.edu.uniquindio.application.exceptions.BadRequestException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.model.HostProfile;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.Role;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.repositories.HostRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.security.JWTUtils;
import co.edu.uniquindio.application.validators.ImageValidators;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

@Mock
private UserRepository userRepository;
@Mock private HostRepository hostRepository;
@Mock private UserMapper userMapper;
@Mock private PasswordEncoder passwordEncoder;
@Mock private JWTUtils jwtUtils;
@Mock private ImageValidators imageValidators;

    @InjectMocks
    private UserServiceImpl userService;
    //private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userMapper,
                userRepository,
                passwordEncoder,
                jwtUtils,
                hostRepository,
                imageValidators
        );
    }

    @Test
    void create_WhenEmailDoesNotExist_ShouldSaveUser() throws Exception {
        // Arrange
        CreateUserDTO dto = new CreateUserDTO("Ana", "Tejada", "ana@mail.com", "123", LocalDate.of(2005, 3, 13),"Colombia", "natalia.png", "123",  Role.USER);
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword("encoded1234");

        // Simulamos comportamientos
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(user);
       // when(passwordEncoder.encode(dto.password())).thenReturn("encoded1234");

        // Act
        userService.create(dto);

        // Assert
        verify(userRepository).save(user); // Verifica que se haya guardado el usuario
    }

    @Test
    void create_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        CreateUserDTO dto = new CreateUserDTO("Ana", "Tejada", "ana@mail.com", "123", LocalDate.of(2005, 3, 13),"Colombia", "natalia.png", "123",  Role.USER);
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        // Act & Assert
        assertThrows(ValueConflictException.class, () -> userService.create(dto));
        verify(userRepository, never()).save(any());
    }
    @Test
    void get_WhenUserExists_ShouldReturnUserDTO() throws Exception {
        User user = new User();
        user.setId("1");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(new UserDTO("Ana","ana@mail.com", "foto.png", LocalDate.of(2005, 3, 12), Role.USER, LocalDateTime.of(2020,12,13, 2, 45)
        ));

        UserDTO dto = userService.get("1");

        assertEquals("ana@mail.com", dto.email());
        verify(userRepository).findById("1");
    }
    @Test
    void get_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.get("1"));
    }
    @Test
    void edit_WhenUserExists_ShouldUpdateUser() throws Exception {
        User user = new User();
        user.setId("1");
        UpdateUserDto updateDto = new UpdateUserDto("Ana","321554", "url.png",LocalDate.of(2005, 3, 13 ));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(imageValidators.isValid(updateDto.photoUrl())).thenReturn(true);

        userService.edit("1", updateDto);

        verify(userMapper).updateUserFromDto(updateDto, user);
        verify(userRepository).save(user);
    }
    @Test
    void addHostData_WhenValid_ShouldUpdateHostProfile() throws Exception {
        User user = new User();
        user.setId("1");
        HostProfile host = new HostProfile();
        HostDTO dto = new HostDTO("Descripción válida", List.of("doc1.pdf"));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(hostRepository.findByUserId("1")).thenReturn(Optional.of(host));

        userService.addHostData("1", dto);

        assertTrue(user.getIsHost());
        verify(userRepository).save(user);
        verify(hostRepository).save(host);
    }
    @Test
    void delete_WhenPasswordMatches_ShouldSetInactive() throws Exception {
        User user = new User();
        user.setId("1");
        user.setPassword(passwordEncoder.encode("1234"));
        DeleteUserDTO dto = new DeleteUserDTO("1234");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);

        userService.delete("1", dto);

        assertEquals(State.INACTIVE, user.getState());
        verify(userRepository).save(user);
    }
    @Test
    void delete_WhenPasswordWrong_ShouldThrowException() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("1234"));
        DeleteUserDTO dto = new DeleteUserDTO("wrong");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);

        assertThrows(ValueConflictException.class, () -> userService.delete("1", dto));
        verify(userRepository, never()).save(any());
    }
    @Test
    void login_WhenCredentialsValid_ShouldReturnToken() throws Exception {
        User user = new User();
        user.setId("1");
        user.setEmail("ana@mail.com");
        user.setName("ana");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole(Role.USER);

        LoginDTO dto = new LoginDTO("ana@mail.com", "1234");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(any(), any())).thenReturn("token123");

        TokenDTO token = userService.login(dto);

        assertEquals("token123", token.token());
    }
    @Test
    void login_WhenUserNotFound_ShouldThrowException() {
        LoginDTO dto = new LoginDTO("noexiste@mail.com", "1234");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.login(dto));
    }
    @Test
    void changePassword_WhenOldPasswordMatches_ShouldUpdatePassword() throws Exception {
        User user = new User();
        user.setPassword(passwordEncoder.encode("1234"));
        UpdatePasswordDTO dto = new UpdatePasswordDTO("1234", "nueva");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.old_password(), user.getPassword())).thenReturn(true);

        userService.changePassword("1", dto);

        verify(userRepository).save(user);
    }
    @Test
    void changePassword_WhenOldPasswordWrong_ShouldThrowException() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("1234"));
        UpdatePasswordDTO dto = new UpdatePasswordDTO("mal", "nueva");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.old_password(), user.getPassword())).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.changePassword("1", dto));
    }


}


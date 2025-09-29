package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.RecoverDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.UnauthorizedException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.PasswordResetCode;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.PasswordResetCodeRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public void create(CreateUserDTO createUserDTO) throws Exception {

        //Verificar si el email ya existe
        if (userRepository.existsByEmail(createUserDTO.email())) {
            throw new ValueConflictException("El email ya está registrado");
        }
        // Crear usuario usando MapStruct
        User user = userMapper.toEntity(createUserDTO);

        // Encriptar contraseña con BCrypt (IMPORTANTE)
        //user.setPassword(passwordEncoder.encode(createUserDTO.password()));

        //Guardar en BD
        userRepository.save(user);
    }

    @Override
    public UserDTO get(String id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        return userMapper.toUserDTO(optionalUser.get());
    }

    @Override
    public UserDTO edit(String id, UpdateUserDto updateUserDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //Validacion foto de perfil
        if (updateUserDto.photoUrl() != null && !imageValidator.isValid(updateUserDto.photoUrl())) {
            throw new ValueConflictException("El formato de imagen no es valido");
        }
        userMapper.updateUserFromDto(updateUserDto, user);

        User updateUser = userRepository.save(user);
        return userMapper.toUserDTO(updateUser);
    }

    @Override
    public UserDTO addHostData(String id, String description) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if(description == null || description.isBlank()){
            throw new ValueConflictException("Descripcion requerida para ser anfitrion");
        }
        user.setDescription(description.trim());
        user.setIsHost(true);

        return userMapper.toUserDTO(userRepository.save(user));


    }

    @Override
    public void delete(String id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        User user = optionalUser.get();
        user.setState(State.INACTIVE);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}

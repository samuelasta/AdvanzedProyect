package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.hostDTO.HostDTO;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.DeleteUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final BCryptPasswordEncoder cryptPasswordEncoder;


    @Override
    public void create(CreateUserDTO createUserDTO) throws Exception {

        //Verificar si el email ya existe
        if (userRepository.existsByEmail(createUserDTO.email())) {
            throw new ValueConflictException("El email ya está registrado");
        }
        // Crear usuario usando MapStruct
        User user = userMapper.toEntity(createUserDTO);

        // Encriptar contraseña con BCrypt (IMPORTANTE)
        user.setPassword(encode(createUserDTO.password()));

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
    public UserDTO addHostData(String id, HostDTO hostDTO) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if(hostDTO.description() == null || hostDTO.description().isBlank()){
            throw new ValueConflictException("Descripcion requerida para ser anfitrion");
        }
        user.setDescription(hostDTO.description().trim());
        //Agreagr el atributo de lista de documentos al host
        user.setIsHost(true);

        return userMapper.toUserDTO(userRepository.save(user));


    }

    @Override
    public void delete(String id, DeleteUserDTO deleteUserDTO) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        if(!passwordEncoder.matches(deleteUserDTO.password(), optionalUser.get().getPassword())){
            throw new ValueConflictException("La contraseña es incorrecta");
        }

        User user = optionalUser.get();
        user.setState(State.INACTIVE);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    // para encriptar la contraseña
    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}

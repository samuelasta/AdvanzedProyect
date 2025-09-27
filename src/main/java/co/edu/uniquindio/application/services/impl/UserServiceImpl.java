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
import co.edu.uniquindio.application.repositories.PasswordResetCodeRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordResetCodeRepository passwordResetCodeRepository;

    @Override
    public void create(CreateUserDTO createUserDTO) throws Exception {
        Optional<User> findUser = userRepository.findByEmail(createUserDTO.email());
        if(findUser.isPresent()) {
            throw new ValueConflictException("El email ya existe");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userMapper.toEntity(createUserDTO);
        user.setPassword(encode(createUserDTO.password()));
        userRepository.save(user);

    }

    @Override
    public void edit(String id, UpdateUserDto userDTO) throws Exception {

    }


    // para encriptar la contraseña
    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }


    @Override
    public void delete(String id) throws Exception {

    }

    //devuelve datos del usuario según el id, hecho
    @Override
    public UserDTO get(String id) throws Exception {

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return userMapper.toUserDTO(user.get());
        }
        throw new ResourceNotFoundException("usuario no encontrado:)");
    }


    //preguntar a rojo si por el codigo que me llega puedo buscar en la base de datos
    @Override
    public void resetPassword(RecoverDTO recoverDTO) throws Exception {
        //buscar que exista el usuario por su email
        User user = userRepository.findByEmail(recoverDTO.email())
        .orElseThrow(() -> new ResourceNotFoundException("usuario no encontrado"));

        // buscar el código de recuperación
        PasswordResetCode resetCode = passwordResetCodeRepository.findByCode(recoverDTO.code());
        if(resetCode == null) {
            throw new ResourceNotFoundException("El code no existe");
        }

        ///verificar si peretenece al usurio
        if(!resetCode.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("El código no pertenece a este usuario");
        }

        //verificar si ya expiró (15 mnts)
        if(resetCode.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(15))){
            throw new ValueConflictException("El código de recuperación ha expirado");
        }

        // cambiamos la contraseña y guardamos el usuario
        user.setPassword(encode(recoverDTO.new_password()));
        userRepository.save(user);

    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {
        return null;
    }

}

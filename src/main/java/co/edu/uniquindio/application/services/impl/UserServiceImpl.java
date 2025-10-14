package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.hostDTO.HostDTO;
import co.edu.uniquindio.application.dto.usersDTOs.*;
import co.edu.uniquindio.application.exceptions.BadRequestException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.HostProfile;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.Role;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.repositories.HostRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.security.JWTUtils;
import co.edu.uniquindio.application.services.ImageService;
import co.edu.uniquindio.application.services.UserService;
import co.edu.uniquindio.application.validators.ImageValidators;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final BCryptPasswordEncoder cryptPasswordEncoder;
    private final JWTUtils jwtUtils;
    private final HostRepository hostRepository;
    private final ImageValidators  imageValidators;

    @Override
    @Transactional
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


        if(createUserDTO.role() == Role.HOST){
            HostProfile host = new HostProfile();
            host.setUser(user);
            host.setId(user.getId());
            hostRepository.save(host);

        }

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
    public void edit(String id, UpdateUserDto updateUserDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validacion foto de perfil
        if (updateUserDto.photoUrl() != null && !imageValidators.isValid(updateUserDto.photoUrl())) {
            throw new ValueConflictException("El formato de imagen no es valido");
        }
        userMapper.updateUserFromDto(updateUserDto, user);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addHostData(String id, HostDTO hostDTO) throws Exception {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        HostProfile host = hostRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de anfitrión no encontrado"));

        if(hostDTO.description() == null || hostDTO.description().isBlank()){
            throw new ValueConflictException("Descripcion requerida para ser anfitrion");
        }
        user.setDescription(hostDTO.description().trim());
        //Agreagr el atributo de lista de documentos al host
        host.setDocuments(hostDTO.legal_documents());
        user.setIsHost(true);

        // actualizamos en la base de datos
        userRepository.save(user);
        hostRepository.save(host);

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

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(loginDTO.email());

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("El usuario no existe");
        }

        User user = optionalUser.get();

        if(user.getState() == State.INACTIVE){
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        // Verificar si la contraseña es correcta usando el PasswordEncoder
        if(!passwordEncoder.matches(loginDTO.password(), user.getPassword())){
            throw new ResourceNotFoundException("El usuario no existe");
        }

        String token = jwtUtils.generateToken(user.getId(), createClaims(user));
        System.out.println(user.getId() + "" + user.getRole().toString() );
        return new TokenDTO(token);
    }

    @Override
    public void changePassword(String id, UpdatePasswordDTO updatePasswordDTO) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(updatePasswordDTO.old_password(), user.getPassword())){
            throw new BadRequestException("la contraseña no coincide");
        }

        // se actualiza la contraseña y guardamos el cambio
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.new_password()));
        userRepository.save(user);
    }

    private Map<String, String> createClaims(User user){
        return Map.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "role", "ROLE_"+user.getRole().name()
        );
    }

    // para encriptar la contraseña
    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    
}

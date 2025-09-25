package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final UserRepository userRepository;

    @Override
    public void create(CreateUserDTO createUserDTO) throws Exception {
        Optional<User> findUser = userRepository.findByEmail(createUserDTO.email());
        if(findUser.isPresent()) {
            throw new ValueConflictException("El email ya existe");
        }
        User user = userMapper.toEntity(createUserDTO);
        userRepository.save(user);

    }

    @Override
    public void edit(String id, UpdateUserDto userDTO) throws Exception {

    }

    private boolean isEmailDuplicated(String email){
        return userStore.values().stream().anyMatch(
                u -> u.getEmail().equalsIgnoreCase(email)
        );
    }


    // para encriptar la contraseña
    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }


    @Override
    public void delete(String id) throws Exception {
        User user = userStore.get(id);

        if(user == null){
            throw new ResourceNotFoundException("usuario no encontrado");
        }
        userStore.remove(id);
    }

    //devuelve datos del usuario según el id
    @Override
    public UserDTO get(String id) throws Exception {

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return userMapper.toUserDTO(user.get());
        }
        throw new ResourceNotFoundException("usuario no encontrado:)");
    }

    @Override
    public List<UserDTO> listAll() {
        List<UserDTO> list = new ArrayList<>();

        for (User user : userStore.values()) {
            list.add(userMapper.toUserDTO(user));
        }

        return list;
    }

}

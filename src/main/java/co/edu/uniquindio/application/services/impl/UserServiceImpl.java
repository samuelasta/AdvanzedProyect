package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.UserMapper;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    @Override
    public void create(CreateUserDTO userDTO) throws Exception {

        /*
        if(isEmailDuplicated(userDTO.email())){
            throw new ValueConflictException("El correo electrónico electronico ya está en uso.");
        }

        User newUser = userMapper.toEntity(userDTO);
        // coframos la contraseña
        newUser.setPassword(encode(newUser.getPassword()));

        userStore.put(newUser.getId(), newUser);*/
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

        User user = userStore.get(id);
        if(user == null){
            throw new ResourceNotFoundException("El usuario no existe");
        }

        return userMapper.toUserDTO(user);
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

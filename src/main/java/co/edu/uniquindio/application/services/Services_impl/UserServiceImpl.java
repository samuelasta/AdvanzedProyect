package co.edu.uniquindio.application.services.Services_impl;

import co.edu.uniquindio.application.dto.CreateUserDTO;
import co.edu.uniquindio.application.dto.UpdateUserDto;
import co.edu.uniquindio.application.dto.UserDTO;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService
{
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    @Override
    public void create(CreateUserDTO userDTO) throws Exception {
        if(isEmailDuplicated(userDTO.email())){
            throw new Exception("El correo electrónico ya está en uso.");
        }

        User newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name(userDTO.name())
                .email(userDTO.email())
                .phone(userDTO.phone())
                .role(userDTO.role())
                .birthday(userDTO.birthDay())
                .photoUrl(userDTO.photoUrl())
                .password(userDTO.password())
                .createdAt(LocalDateTime.now())
                .status(State.ACTIVE)
                .build();

        userStore.put(newUser.getId(), newUser);
    }

    private boolean isEmailDuplicated(String email){
        return userStore.values().stream().anyMatch(
                u -> u.getEmail().equalsIgnoreCase(email)
        );
    }

    @Override
    public void edit(UpdateUserDto userDTO) {
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<UserDTO> listAll() {
        return List.of();
    }

}

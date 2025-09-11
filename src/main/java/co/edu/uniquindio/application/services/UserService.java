package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.CreateUserDTO;
import co.edu.uniquindio.application.dto.UpdateUserDto;
import co.edu.uniquindio.application.dto.UserDTO;

import java.util.List;

public interface UserService {


     void create(CreateUserDTO userDTO);
     void edit(UpdateUserDto userDTO);
     void delete(String id);
     List<UserDTO> listAll();

}

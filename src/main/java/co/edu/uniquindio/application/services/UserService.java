package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.RecoverDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;

import java.util.List;

public interface UserService {


     void create(CreateUserDTO userDTO) throws Exception;
     void edit(String id, UpdateUserDto userDTO) throws Exception;
     void delete(String id) throws Exception;
     UserDTO get(String id) throws Exception;
     void resetPassword(RecoverDTO recoverDTO) throws Exception;
     TokenDTO login(LoginDTO loginDTO) throws Exception;

}

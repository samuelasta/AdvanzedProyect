package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.authDTO.LoginDTO;
import co.edu.uniquindio.application.dto.authDTO.RecoverDTO;
import co.edu.uniquindio.application.dto.authDTO.TokenDTO;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.User;

import java.util.List;

public interface UserService {


     //Registro de Usuarios - POST /users
     void create(CreateUserDTO userDTO) throws Exception;

     //Obtener usuario por id - GET /user/{id}
     UserDTO get(String id) throws Exception;

     //Actualizar/Editar datos del usuario - PUT /users/{id}
     void edit(String id, UpdateUserDto userDTO) throws Exception;

     //que pasa si el usuario al principio no quiso poner un dato y leugo si, preguntar profe
     UserDTO addHostData(String id, String description) throws Exception;

     //Eliminar Usuario - DELETE /user/{id}
     void delete(String id) throws Exception;

     User findByEmail(String email);




}

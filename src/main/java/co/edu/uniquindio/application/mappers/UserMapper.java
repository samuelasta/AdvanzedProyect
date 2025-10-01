package co.edu.uniquindio.application.mappers;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    //convierte de dto a entidad y viceversa, crea automaticamente el id, estado y fecha de creación de la cuenta
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "state", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "password", ignore = true)//Se maneja en el servicio
    @Mapping(target = "isHost", constant = "false")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    @Mapping(target = "role", ignore = true)

    User toEntity(CreateUserDTO createUserDTO);

    UserDTO toUserDTO(User user);


    //metodo para actualizar usuario existente
    //¿Por que no se puede usar beenMapping?
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isHost", ignore = true)
    @Mapping(target = "description", ignore = true)
    void updateUserFromDto(UpdateUserDto updateUserDTO, @MappingTarget User user);





}

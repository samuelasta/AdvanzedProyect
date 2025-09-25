package co.edu.uniquindio.application.mappers;
import co.edu.uniquindio.application.dto.usersDTOs.CreateUserDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.dto.usersDTOs.UserDTO;
import co.edu.uniquindio.application.model.User;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    //convierte de dto a entidad y viceversa, crea automaticamente el id, estado y fecha de creación de la cuenta
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "state", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")

    User toEntity(CreateUserDTO createUserDTO);

    UserDTO toUserDTO(User user);


}

package co.edu.uniquindio.application.mappers;

import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDetailDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UserDetailDTO;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccommodationDetailMapper {

    @Mapping(source = "user", target = "userDetailDTO")
    @Mapping(source = "location.coordinates.latitude", target = "latitude")
    @Mapping(source = "location.coordinates.longitude", target = "longitude")

    AccommodationDetailDTO toAccommodationDetailDTO(Accommodation accommodation);

    UserDetailDTO toUserDetailDTO(User user);

}

package co.edu.uniquindio.application.mappers;

import co.edu.uniquindio.application.dto.accommodationDTO.CreateAccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.UpdateDTO;
import co.edu.uniquindio.application.dto.usersDTOs.UpdateUserDto;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Location;
import co.edu.uniquindio.application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccommodationMapper {

//convierte de dto a entidad y viceversa, crea automaticamente:
    @Mapping(target = "location.country", source = "country")
    @Mapping(target = "location.department", source = "department")
    @Mapping(target = "location.city", source = "city")
    @Mapping(target = "location.neighborhood", source = "neighborhood")
    @Mapping(target = "location.street", source = "street")
    @Mapping(target = "location.postalCode", source = "postalCode")
    @Mapping(target = "location.coordinates.latitude", source = "latitude")
    @Mapping(target = "location.coordinates.longitude", source = "longitude")

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "state", constant = "ACTIVE")
    @Mapping(target = "totalRatings", constant = "0")
    @Mapping(target = "averageRatings", constant = "0")
    @Mapping(target = "comments", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "pics_url", source = "picsUrl")
    //@Mapping(target = "user", source = "user")

    Accommodation toEntity(CreateAccommodationDTO createAccommodationDTO);


    CreateAccommodationDTO toCreateAccommodationDTO(Accommodation accommodation);

    void updateAccommodationFromDto(UpdateDTO dto, @MappingTarget Accommodation accommodation);

}

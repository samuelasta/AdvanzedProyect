package co.edu.uniquindio.application.mappers;

import ch.qos.logback.core.model.ComponentModel;
import co.edu.uniquindio.application.dto.accommodationDTO.GetForUpdateDTO;
import co.edu.uniquindio.application.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ForUpdateMapper {

    @Mapping(target = "country", source = "location.country")
    @Mapping(target = "department", source = "location.department")
    @Mapping(target = "city", source = "location.city")
    @Mapping(target = "neighborhood", source = "location.neighborhood")
    @Mapping(target = "street", source = "location.street")
    @Mapping(target = "postalCode", source = "location.postalCode")
    @Mapping(target = "latitude", source = "location.coordinates.latitude")
    @Mapping(target = "longitude", source = "location.coordinates.longitude")
    GetForUpdateDTO toUpdateDTO(Accommodation accommodation);
}


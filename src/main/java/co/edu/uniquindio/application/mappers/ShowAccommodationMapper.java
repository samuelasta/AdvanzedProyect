package co.edu.uniquindio.application.mappers;


import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDTO;
import co.edu.uniquindio.application.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShowAccommodationMapper {

    // Para el listado (resumen)
    @Mapping(target = "photo_url", expression = "java(accommodation.getPics_url() != null && !accommodation.getPics_url().isEmpty() ? accommodation.getPics_url().get(0) : null)")
    AccommodationDTO toAccommodationDTO(Accommodation accommodation);


    // falta el average rating +++



}

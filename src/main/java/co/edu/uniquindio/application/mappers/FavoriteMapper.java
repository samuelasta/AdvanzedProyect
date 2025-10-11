package co.edu.uniquindio.application.mappers;

import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Favorites;
import co.edu.uniquindio.application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FavoriteMapper {


    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "accommodation", target = "accommodation")

    Favorites favoritesToFavorites(Accommodation accommodation, User user);
}

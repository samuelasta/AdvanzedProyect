package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.dto.usersDTOs.UserDetailDTO;
import co.edu.uniquindio.application.model.enums.Amenities;

import java.util.List;

public record AccommodationDetailDTO(String id, float latitude,
                                     float longitude,
                                     double price,
                                     List<String> pics_url,
                                     String description,
                                     List<Amenities> amenities,
                                     String title,
                                     int capacity,
                                     double averageRatings,
                                     UserDetailDTO userDetailDTO
                                     ) {
}

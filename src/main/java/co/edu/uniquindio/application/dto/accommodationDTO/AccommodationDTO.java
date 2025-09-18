package co.edu.uniquindio.application.dto.accommodationDTO;

import co.edu.uniquindio.application.model.Location;

import java.util.List;

public record AccommodationDTO(String title,
                               double price,
                               String photo_url,
                               double average_rating,
                               String city
                               ) {
}

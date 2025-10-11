package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDetailDTO;

import java.util.List;

public interface FavoriteService {

    void add(String accommodationId) throws Exception;
    void delete(String accommodationId) throws Exception;
    List<AccommodationDTO> getAll(int page) throws Exception;
}

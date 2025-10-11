package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDTO;
import co.edu.uniquindio.application.exceptions.ForbiddenException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.exceptions.ValueConflictException;
import co.edu.uniquindio.application.mappers.FavoriteMapper;
import co.edu.uniquindio.application.mappers.ShowAccommodationMapper;
import co.edu.uniquindio.application.model.Accommodation;
import co.edu.uniquindio.application.model.Favorites;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.repositories.AccommodationRepository;
import co.edu.uniquindio.application.repositories.FavoriteRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final AccommodationRepository accommodationRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final CurrentUserServiceImpl currentUserServiceImpl;
    private final FavoriteMapper  favoriteMapper;
    private final ShowAccommodationMapper  showAccommodationMapper;


    @Override
    public void add(String accommodationId) throws Exception {
        // verificar si existe el alojamiento
        Optional<Accommodation> accommodation = accommodationRepository.findById(accommodationId);
        if (accommodation.isEmpty()) {
            throw new ResourceNotFoundException("No se encontró el alojamiento");
        }

        // si no existe el usuario logueado por algun motivo
        Optional<User> user = userRepository.findById(currentUserServiceImpl.getCurrentUser());
        if (user.isEmpty()) {
            throw new ForbiddenException("No puedes realizar esto");
        }

        // verificar que ya no se haya guardado antes como favorito
        boolean exists = favoriteRepository.existsByUserIdAndAccommodationId(user.get().getId(), accommodationId);
        if(exists){
            throw new ValueConflictException("Ya tienes este alojamiento en tus favoritos");
        }

        Favorites favorites = favoriteMapper.favoritesToFavorites(accommodation.get(), user.get());
        favoriteRepository.save(favorites);

    }

    // para eliminar el alojamiento de favoritos
    @Override
    @Transactional
    public void delete(String accommodationId) throws Exception {

        Optional<User> user = userRepository.findById(currentUserServiceImpl.getCurrentUser());
        if (user.isEmpty()) {
            throw new ForbiddenException("No puedes realizar esto");
        }

        // no sabia que esto se podía pero queda muy resumido, mucho mejor
        int deleted = favoriteRepository.deleteByUserIdAndAccommodationId(user.get().getId(), accommodationId);
        if (deleted == 0) {
            throw new ResourceNotFoundException("No se encontro el alojamiento");
        }

    }

    // obtiene todos los alojamientos favoritos (paginación)
    @Override
    public List<AccommodationDTO> getAll(int page) throws Exception {

        String currentUser = currentUserServiceImpl.getCurrentUser();
        Pageable pageable = PageRequest.of(page, 10);
        Page<Accommodation> accommodations = favoriteRepository.findAccommodationsByUserId(currentUser, pageable);

        if(accommodations.isEmpty()){
            // quise no enviar una expepción si no tiene favoritos, desde el front ya le doy manejo
            return List.of();
        }

        // mapeamos para enviar solo los datos que queremos que los usuarios vean
        return accommodations.stream()
                .map(showAccommodationMapper::toAccommodationDTO).toList();
    }
}

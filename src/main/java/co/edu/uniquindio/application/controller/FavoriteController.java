package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDTO;
import co.edu.uniquindio.application.dto.accommodationDTO.AccommodationDetailDTO;
import co.edu.uniquindio.application.services.CurrentUserService;
import co.edu.uniquindio.application.services.FavoriteService;
import co.edu.uniquindio.application.services.FavoriteService;
import co.edu.uniquindio.application.services.impl.FavoriteServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteServiceImpl favoriteServiceImpl;
    private final CurrentUserService currentUserServiceImpl;

    // para agregar el alojamiento a favoritos
    @PostMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> addToFavorite(@PathVariable String id) throws Exception {
        favoriteServiceImpl.add(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Alojamiento añadido a tus favoritos "));
    }

    // para eliminar el alojamiento de favoritos
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteFromFavorite(@PathVariable String id) throws Exception {
        favoriteServiceImpl.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Alojamiento eliminado de tus favoritos"));
    }

    // para obtener los alojamientos favoritos (paginados)
    @GetMapping("/{page}")
    public ResponseEntity<ResponseDTO<List<AccommodationDTO>>> getAll(@PathVariable int page) throws Exception {
        String currentUser = currentUserServiceImpl.getCurrentUser();
        List<AccommodationDTO> list = favoriteServiceImpl.getAll(page);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, list));
    }

}

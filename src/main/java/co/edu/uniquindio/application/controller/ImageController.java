package co.edu.uniquindio.application.controller;

import co.edu.uniquindio.application.dto.ResponseDTO;
import co.edu.uniquindio.application.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO<Map>> upload(@RequestParam("file") MultipartFile image) throws Exception{
        Map response = imageService.upload(image);
        return ResponseEntity.ok( new ResponseDTO<>(false, response) );
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<String>> delete(@RequestParam("id") String id) throws Exception{
        imageService.delete(id);
        return ResponseEntity.ok( new ResponseDTO<>(false, "Imagen eliminada exitosamente") );
    }

}

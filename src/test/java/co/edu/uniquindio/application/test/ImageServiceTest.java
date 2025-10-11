package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.services.impl.ImageServiceImpl;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
    }

    @Test
    void upload_WhenValidImage_ShouldReturnResponseMap() throws Exception {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("imagen.png");
        when(multipartFile.getBytes()).thenReturn("fakeImageData".getBytes());

        Map<String, Object> mockResponse = Map.of("url", "https://cloudinary.com/img123");

        // Mockeamos Cloudinary y su uploader
        try (MockedConstruction<Cloudinary> mocked = mockConstruction(Cloudinary.class,
                (mockCloudinary, context) -> {
                    Uploader uploader = mock(Uploader.class);
                    when(mockCloudinary.uploader()).thenReturn(uploader);
                    when(uploader.upload(any(File.class), anyMap())).thenReturn(mockResponse);
                })) {

            // Act
            ImageServiceImpl imageService = new ImageServiceImpl();
            Map result = imageService.upload(multipartFile);

            // Assert
            assertEquals("https://cloudinary.com/img123", result.get("url"));
        }
    }

    @Test
    void delete_WhenValidId_ShouldReturnResponseMap() throws Exception {
        // Arrange
        Map<String, Object> mockResponse = Map.of("result", "ok");

        try (MockedConstruction<Cloudinary> mocked = mockConstruction(Cloudinary.class,
                (mockCloudinary, context) -> {
                    Uploader uploader = mock(Uploader.class);
                    when(mockCloudinary.uploader()).thenReturn(uploader);
                    when(uploader.destroy(eq("img123"), anyMap())).thenReturn(mockResponse);
                })) {

            // Act
            ImageServiceImpl imageService = new ImageServiceImpl();
            Map result = imageService.delete("img123");

            // Assert
            assertEquals("ok", result.get("result"));
        }
    }

    @Test
    void convert_WhenIOException_ShouldThrowException() throws Exception {
        // Arrange
        MultipartFile badFile = mock(MultipartFile.class);
        when(badFile.getOriginalFilename()).thenReturn("testImage"); // ✅ nombre válido
        when(badFile.getBytes()).thenThrow(new IOException("Error al leer bytes"));

        ImageServiceImpl imageService = new ImageServiceImpl();

        // Act & Assert
        assertThrows(IOException.class, () -> imageService.upload(badFile));
    }

}

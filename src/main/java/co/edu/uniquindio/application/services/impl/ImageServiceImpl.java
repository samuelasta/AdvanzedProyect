package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.services.ImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    public ImageServiceImpl(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dje3qr8tq");
        config.put("api_key", "693381465632364");
        config.put("api_secret", "5cICFV1EvZ-E8FPCAyNSh5WGUt0");
        cloudinary = new Cloudinary(config);
    }

    @Override
    public Map upload(MultipartFile image) throws Exception {
        File file = convert(image);
        // Reemplace "folder" si quiero guardar las imagenes en otra carpeta de cloudinary
        return cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "AdvanzedProyect"));
    }

    @Override
    public Map delete(String imageId) throws Exception {
        return cloudinary.uploader().destroy(imageId, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile image) throws IOException {
        File file = File.createTempFile(image.getOriginalFilename(), null);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(image.getBytes());
        fos.close();
        return file;
    }
}

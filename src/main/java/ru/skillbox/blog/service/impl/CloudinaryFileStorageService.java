package ru.skillbox.blog.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.exceptions.ApiException;
import ru.skillbox.blog.service.FileStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "cloudinary-file-storage-service", havingValue = "true")
public class CloudinaryFileStorageService implements FileStorageService {

    private final Cloudinary cloudinary;

    public CloudinaryFileStorageService() {
        this.cloudinary = new Cloudinary();
    }

    @Override
    public String saveUploadedImage(MultipartFile file) {
        return saveImage("upload", file, SOURCE_SAVER);
    }

    @Override
    public String saveAvatar(MultipartFile file) {
        return saveImage("avatars", file, AVATAR_SAVER);
    }

    private String saveImage(String subFolder, MultipartFile multipartFile, ImageSaver imageSaver) {
        String imageType = getSupportedExtension(multipartFile.getContentType());
        if (imageType == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    Map.of("image", "Неподдерживаемый формат файла"));
        }
        try {
            String publicId = imageSaver.transformAndSaveImage(multipartFile, imageType);
            return "/" + subFolder + "/" + publicId;
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, null);
        }
    }

    private interface ImageSaver {
        String transformAndSaveImage(MultipartFile sourceFile, String imageType)
                throws IOException;
    }

    private final ImageSaver SOURCE_SAVER = new ImageSaver() {
        @Override
        public String transformAndSaveImage(MultipartFile sourceFile, String imageType) throws IOException {
            return (String) cloudinary.uploader().upload(sourceFile.getBytes(), Map.of()).get("public_id");
        }
    };

    private final ImageSaver AVATAR_SAVER = new ImageSaver() {
        @Override
        public String transformAndSaveImage(MultipartFile sourceFile, String imageType) throws IOException {
            return (String) cloudinary.uploader().upload(sourceFile.getBytes(),
                    Map.of("transformation",
                            new Transformation<>().width(36).height(36).crop("fit"))
            ).get("public_id");
        }
    };

    private String getSupportedExtension(String contentType) {
        if (contentType == null) {
            return null;
        }
        switch (contentType) {
            case "image/jpeg": return "jpg";
            case "image/png": return "png";
            default: return null;
        }
    }

    @Override
    public Resource getImage(String filename) {
        String publicId = extractPublicId(filename);
        Url url = cloudinary.url().publicId(publicId);
        try {
            return new UrlResource(url.generate());
        } catch (MalformedURLException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, null);
        }
    }

    private static final String AVATARS_PREFIX = "/avatars/";
    private static final String UPLOAD_PREFIX = "/upload/";
    
    private String extractPublicId(String filename) {
        if (filename.startsWith(AVATARS_PREFIX)) {
            return filename.substring(AVATARS_PREFIX.length());
        }
        if (filename.startsWith(UPLOAD_PREFIX)) {
            return filename.substring(UPLOAD_PREFIX.length());
        }
        throw new AccessDeniedException(filename);
    }

    @Override
    public void deleteFile(String filename) {
        String publicId = extractPublicId(filename);
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, null);
        }
    }
}

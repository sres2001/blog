package ru.skillbox.blog.service.impl;

import org.imgscalr.Scalr;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.exceptions.ApiException;
import ru.skillbox.blog.service.FileStorageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

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
        String relativePath = subFolder + "/" + generateRandomPath() + "." + imageType;
        try {
            File file = Paths.get(relativePath).toFile();
            file.getParentFile().mkdirs();
            imageSaver.transformAndSaveImage(multipartFile, file, imageType);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, null);
        }
        return "/" + relativePath;
    }

    private interface ImageSaver {
        void transformAndSaveImage(MultipartFile sourceFile, File targetFile, String imageType)
                throws IOException;
    }

    private static final ImageSaver SOURCE_SAVER = (sourceFile, targetFile, imageType) -> {
        try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
             ReadableByteChannel readableByteChannel = Channels.newChannel(sourceFile.getInputStream())) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    };

    private static final ImageSaver AVATAR_SAVER = (sourceFile, targetFile, imageType) -> {
        try (InputStream sourceStream = sourceFile.getInputStream()) {
            BufferedImage image = ImageIO.read(sourceStream);
            if (image == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        Map.of("image", "Неподдерживаемый формат файла"));
            }
            BufferedImage newImage = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, 36, 36, Scalr.OP_ANTIALIAS);
            ImageIO.write(newImage, imageType, targetFile);
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

    private String generateRandomPath() {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        return s.substring(0, 2) + '/' + s.substring(2, 4) + '/' + s.substring(4, 6) + '/' + s.substring(6);
    }

    @Override
    public Resource getImage(String filename) {
        if (!isPathToUploadedImage(filename)) {
            throw new AccessDeniedException(filename);
        }
        return new FileSystemResource(Paths.get(filename.substring(1)).toFile());
    }

    private boolean isPathToUploadedImage(String filename) {
        return filename.matches("^/(?:upload|avatars)/[a-f0-9/]+\\.(?:jpg|png)$");
    }

    @Override
    public void deleteFile(String filename) {
        if (!isPathToUploadedImage(filename)) {
            throw new AccessDeniedException(filename);
        }
        Paths.get(filename.substring(1)).toFile().delete();
    }
}

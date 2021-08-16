package ru.skillbox.blog.service.impl;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.exceptions.ApiException;
import ru.skillbox.blog.service.FileStorageService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public String saveImage(MultipartFile multipartFile) {
        String imageExtension = getSupportedExtension(multipartFile.getContentType());
        if (imageExtension == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    Map.of("image", "Неподдерживаемый формат файла"));
        }
        String relativePath = "upload/" + generateRandomPath() + "." + imageExtension;
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(multipartFile.getInputStream());
            File file = Paths.get(relativePath).toFile();
            file.getParentFile().mkdirs();
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            }
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, null);
        }
        return "/" + relativePath;
    }

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
        return filename.matches("^/upload/[a-f0-9/]+\\.(?:jpg|png)$");
    }
}

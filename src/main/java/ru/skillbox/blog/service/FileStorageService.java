package ru.skillbox.blog.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveUploadedImage(MultipartFile file);

    String saveAvatar(MultipartFile file);

    Resource getImage(String filename);

    void deleteFile(String filename);
}

package ru.skillbox.blog.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveImage(MultipartFile file);

    Resource getImage(String filename);
}

package ru.skillbox.blog.api.request;

import org.springframework.web.multipart.MultipartFile;

public class UpdateProfileWithPhotoRequest extends UpdateProfileRequest {

    private MultipartFile photo;

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}

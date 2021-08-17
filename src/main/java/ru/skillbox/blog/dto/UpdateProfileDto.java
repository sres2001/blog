package ru.skillbox.blog.dto;

import org.springframework.web.multipart.MultipartFile;

public class UpdateProfileDto {

    private int userId;
    private String name;
    private String email;
    private String password;
    private int removePhoto;
    private MultipartFile photo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(int removePhoto) {
        this.removePhoto = removePhoto;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}

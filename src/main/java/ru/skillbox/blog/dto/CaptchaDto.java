package ru.skillbox.blog.dto;

public class CaptchaDto {
    private String secret;
    private byte[] image;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

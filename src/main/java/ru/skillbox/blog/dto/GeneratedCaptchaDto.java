package ru.skillbox.blog.dto;

public class GeneratedCaptchaDto {
    private String id;
    private String code;
    private byte[] pngImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getPngImage() {
        return pngImage;
    }

    public void setPngImage(byte[] pngImage) {
        this.pngImage = pngImage;
    }
}

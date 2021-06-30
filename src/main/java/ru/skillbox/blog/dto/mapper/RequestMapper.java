package ru.skillbox.blog.dto.mapper;

import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.dto.RegisterDto;

public class RequestMapper {

    public static RegisterDto toRegisterDto(RegisterRequest data) {
        RegisterDto dto = new RegisterDto();
        dto.setEmail(data.getEmail());
        dto.setPassword(data.getPassword());
        dto.setName(data.getName());
        dto.setCaptcha(data.getCaptcha());
        dto.setCaptchaSecret(data.getCaptchaSecret());
        return dto;
    }
}

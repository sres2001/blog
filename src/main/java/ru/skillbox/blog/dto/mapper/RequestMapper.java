package ru.skillbox.blog.dto.mapper;

import ru.skillbox.blog.api.request.AddPostRequest;
import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.dto.AddPostRequestDto;
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

    public static AddPostRequestDto toAddPostDto(int userId, AddPostRequest request) {
        AddPostRequestDto dto = new AddPostRequestDto();
        dto.setUserId(userId);
        dto.setTimestamp(request.getTimestamp());
        dto.setActive(request.getActive());
        dto.setTitle(request.getTitle());
        dto.setTags(request.getTags());
        dto.setText(request.getText());
        return dto;
    }
}

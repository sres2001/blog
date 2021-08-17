package ru.skillbox.blog.dto.mapper;

import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.api.request.EditPostRequest;
import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.api.request.UpdateProfileRequest;
import ru.skillbox.blog.dto.EditPostRequestDto;
import ru.skillbox.blog.dto.RegisterDto;
import ru.skillbox.blog.dto.UpdateProfileDto;

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

    public static EditPostRequestDto toEditPostDto(int userId, EditPostRequest request) {
        EditPostRequestDto dto = new EditPostRequestDto();
        dto.setUserId(userId);
        dto.setTimestamp(request.getTimestamp());
        dto.setActive(request.getActive());
        dto.setTitle(request.getTitle());
        dto.setTags(request.getTags());
        dto.setText(request.getText());
        return dto;
    }

    public static UpdateProfileDto toUpdateProfileDto(int userId, UpdateProfileRequest request, MultipartFile photo) {
        UpdateProfileDto dto = new UpdateProfileDto();
        dto.setUserId(userId);
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setRemovePhoto(request.getRemovePhoto());
        dto.setPhoto(photo);
        return dto;
    }
}

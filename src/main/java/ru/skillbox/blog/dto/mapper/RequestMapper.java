package ru.skillbox.blog.dto.mapper;

import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.api.request.ChangePasswordRequest;
import ru.skillbox.blog.api.request.EditPostRequest;
import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.api.request.UpdateProfileRequest;
import ru.skillbox.blog.api.response.SettingsResponse;
import ru.skillbox.blog.dto.*;

public class RequestMapper {

    public static RegisterDto toRegisterDto(RegisterRequest request) {
        RegisterDto dto = new RegisterDto();
        dto.setEmail(request.getEmail());
        dto.setPassword(request.getPassword());
        dto.setName(request.getName());
        dto.setCaptcha(request.getCaptcha());
        dto.setCaptchaSecret(request.getCaptchaSecret());
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

    public static ChangePasswordDto toChangePasswordDto(ChangePasswordRequest request) {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCode(request.getCode());
        dto.setPassword(request.getPassword());
        dto.setCaptcha(request.getCaptcha());
        dto.setCaptchaSecret(request.getCaptchaSecret());
        return dto;
    }

    public static SettingsDto toSettingsDto(SettingsResponse request) {
        SettingsDto dto = new SettingsDto();
        dto.setMultiuserMode(request.isMultiuserMode());
        dto.setPostPremoderation(request.isPostPremoderation());
        dto.setStatisticsIsPublic(request.isStatisticsIsPublic());
        return dto;
    }
}

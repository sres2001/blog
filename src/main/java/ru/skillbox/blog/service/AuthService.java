package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.*;

public interface AuthService {

    CaptchaDto createCaptcha();

    void registerUser(RegisterDto registerDto);

    UserProfileDto authenticateUser(String email, String password);

    UserDto getUser(String email);

    UserProfileDto getUserProfile(String email);

    void updateProfile(UpdateProfileDto toUpdateProfileDto);
}

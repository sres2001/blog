package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.*;

public interface AuthService {

    CaptchaDto createCaptcha();

    void registerUser(RegisterDto dto);

    UserProfileDto authenticateUser(String email, String password);

    UserDto getUser(String email);

    UserProfileDto getUserProfile(String email);

    void updateProfile(UpdateProfileDto dto);

    String restorePassword(String email);

    void changePassword(ChangePasswordDto dto);
}

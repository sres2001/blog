package ru.skillbox.blog.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.skillbox.blog.dto.*;

public interface AuthService {

    CaptchaDto createCaptcha();

    void registerUser(RegisterDto dto);

    UserProfileDto authenticateUser(
            HttpServletRequest request,
            String email,
            String password);

    UserDto getUser(String email);

    UserProfileDto getUserProfile(String email);

    void updateProfile(HttpServletRequest request, UpdateProfileDto dto);

    String restorePassword(String email);

    void changePassword(ChangePasswordDto dto);
}

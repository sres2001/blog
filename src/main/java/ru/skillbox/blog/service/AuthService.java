package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.CaptchaDto;
import ru.skillbox.blog.dto.RegisterDto;
import ru.skillbox.blog.dto.UserDto;
import ru.skillbox.blog.dto.UserProfileDto;
import ru.skillbox.blog.dto.mapper.RegisterResponseDto;

public interface AuthService {

    CaptchaDto createCaptcha();

    RegisterResponseDto registerUser(RegisterDto registerDto);

    UserProfileDto authenticateUser(String email, String password);

    UserDto getUser(String email);

    UserProfileDto getUserProfile(String email);
}

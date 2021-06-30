package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.CaptchaDto;
import ru.skillbox.blog.dto.RegisterDto;
import ru.skillbox.blog.dto.mapper.RegisterResponseDto;

public interface AuthService {

    CaptchaDto createCaptcha();

    RegisterResponseDto registerUser(RegisterDto registerDto);
}

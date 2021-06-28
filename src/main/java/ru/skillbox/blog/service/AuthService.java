package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.CaptchaDto;

public interface AuthService {

    CaptchaDto createCaptcha();
}

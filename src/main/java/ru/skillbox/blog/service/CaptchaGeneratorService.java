package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.GeneratedCaptchaDto;

public interface CaptchaGeneratorService {

    GeneratedCaptchaDto generateCaptcha();
}

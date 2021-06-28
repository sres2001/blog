package ru.skillbox.blog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.dto.CaptchaDto;
import ru.skillbox.blog.dto.GeneratedCaptchaDto;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.model.CaptchaCode;
import ru.skillbox.blog.repository.CaptchaCodeRepository;
import ru.skillbox.blog.service.AuthService;
import ru.skillbox.blog.service.CaptchaGeneratorService;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final CaptchaCodeRepository captchaCodeRepository;
    private final Duration captchaExpiration;
    private final CaptchaGeneratorService captchaGeneratorService;

    public AuthServiceImpl(
            CaptchaCodeRepository captchaCodeRepository,
            @Value("${blog.captchaExpiration}") String captchaExpiration,
            CaptchaGeneratorService captchaGeneratorService
    ) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.captchaExpiration = captchaExpiration == null || captchaExpiration.isEmpty() ? Duration.ofHours(1) :
                Duration.parse(captchaExpiration);
        this.captchaGeneratorService = captchaGeneratorService;
    }

    @Transactional
    @Override
    public CaptchaDto createCaptcha() {
        captchaCodeRepository.deleteOldCaptcha(Date.from(Instant.now().minus(captchaExpiration)));
        GeneratedCaptchaDto generated = captchaGeneratorService.generateCaptcha();
        CaptchaCode entity = new CaptchaCode();
        entity.setCode(generated.getCode());
        entity.setSecretCode(generated.getId());
        entity.setTime(new Date());
        captchaCodeRepository.save(entity);
        return DtoMapper.toCaptchaDto(generated);
    }
}

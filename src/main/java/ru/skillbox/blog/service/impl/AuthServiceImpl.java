package ru.skillbox.blog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.dto.CaptchaDto;
import ru.skillbox.blog.dto.GeneratedCaptchaDto;
import ru.skillbox.blog.dto.RegisterDto;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.dto.mapper.RegisterResponseDto;
import ru.skillbox.blog.model.CaptchaCode;
import ru.skillbox.blog.model.User;
import ru.skillbox.blog.repository.CaptchaCodeRepository;
import ru.skillbox.blog.repository.UserRepository;
import ru.skillbox.blog.service.AuthService;
import ru.skillbox.blog.service.CaptchaGeneratorService;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final CaptchaCodeRepository captchaCodeRepository;
    private final Duration captchaExpiration;
    private final CaptchaGeneratorService captchaGeneratorService;
    private final UserRepository userRepository;

    public AuthServiceImpl(
            CaptchaCodeRepository captchaCodeRepository,
            @Value("${blog.captchaExpiration}") String captchaExpiration,
            CaptchaGeneratorService captchaGeneratorService,
            UserRepository userRepository
    ) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.captchaExpiration = captchaExpiration == null || captchaExpiration.isEmpty() ? Duration.ofHours(1) :
                Duration.parse(captchaExpiration);
        this.captchaGeneratorService = captchaGeneratorService;
        this.userRepository = userRepository;
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

    @Transactional
    @Override
    public RegisterResponseDto registerUser(RegisterDto registerDto) {
        Map<String, String> errors = new HashMap<>();
        CaptchaCode captchaCode = validateRegistrationData(registerDto, errors);
        RegisterResponseDto responseDto = new RegisterResponseDto();
        if (errors.isEmpty()) {
            User user = new User();
            user.setEmail(registerDto.getEmail());
            user.setPassword(sha1(registerDto.getPassword()));
            user.setName(registerDto.getName());
            user.setRegistrationTime(new Date());
            user.setRegistrationTime(new Date());
            userRepository.save(user);
            captchaCodeRepository.delete(captchaCode);
            responseDto.setResult(true);
        } else {
            responseDto.setResult(false);
            responseDto.setErrors(errors);
        }
        return responseDto;
    }

    private CaptchaCode validateRegistrationData(RegisterDto registerDto, Map<String, String> errors) {
        if (registerDto.getEmail() == null || !isEmail(registerDto.getEmail())) {
            errors.put("email", "e-mail указан неверно");
        } else if (userRepository.existsByEmailIgnoreCase(registerDto.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (registerDto.getName() == null || !isUserName(registerDto.getName())) {
            errors.put("name", "Имя указано неверно");
        }
        if (registerDto.getPassword() == null || registerDto.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        CaptchaCode captchaCode = captchaCodeRepository.findBySecretCode(registerDto.getCaptchaSecret());
        if (captchaCode == null || !captchaCode.getCode().equals(registerDto.getCaptcha())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }
        return captchaCode;
    }

    private boolean isEmail(String email) {
        return email.matches("^.+@.+\\..+$");
    }

    private boolean isUserName(String name) {
        return !name.isBlank();
    }

    private static String sha1(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("cannot hash password", e);
        }
        digest.reset();
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }
}

package ru.skillbox.blog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.dto.*;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.exceptions.ApiException;
import ru.skillbox.blog.model.CaptchaCode;
import ru.skillbox.blog.model.User;
import ru.skillbox.blog.repository.CaptchaCodeRepository;
import ru.skillbox.blog.repository.UserRepository;
import ru.skillbox.blog.service.AuthService;
import ru.skillbox.blog.service.CaptchaGeneratorService;
import ru.skillbox.blog.service.PostService;

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
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;

    public AuthServiceImpl(
            CaptchaCodeRepository captchaCodeRepository,
            @Value("${blog.captchaExpiration}") String captchaExpiration,
            CaptchaGeneratorService captchaGeneratorService,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            PostService postService
    ) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.captchaExpiration = captchaExpiration == null || captchaExpiration.isEmpty() ? Duration.ofHours(1) :
                Duration.parse(captchaExpiration);
        this.captchaGeneratorService = captchaGeneratorService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.postService = postService;
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
    public void registerUser(RegisterDto registerDto) {
        CaptchaCode captchaCode = validateRegistrationData(registerDto);
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setName(registerDto.getName());
        user.setRegistrationTime(new Date());
        user.setRegistrationTime(new Date());
        userRepository.save(user);
        captchaCodeRepository.delete(captchaCode);
    }

    private CaptchaCode validateRegistrationData(RegisterDto registerDto) {
        Map<String, String> errors = new HashMap<>();
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
        if (!errors.isEmpty()) {
            throw new ApiException(HttpStatus.OK, errors);
        }
        return captchaCode;
    }

    private boolean isEmail(String email) {
        return email.matches("^.+@.+\\..+$");
    }

    private boolean isUserName(String name) {
        return !name.isBlank();
    }

    @Override
    public UserProfileDto authenticateUser(String email, String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return getUserProfile(email);
    }

    @Override
    public UserDto getUser(String email) {
        return DtoMapper.toUserDto(getUserByEmail(email));
    }

    private User getUserByEmail(String email) {
        return userRepository.getByEmailIgnoreCase(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username %s not found", email)));
    }

    @Override
    public UserProfileDto getUserProfile(String email) {
        User user = getUserByEmail(email);
        long moderationCount = user.isModerator() ? postService.countPostsForModeration() : 0;
        return DtoMapper.toUserProfileDto(user, moderationCount);
    }
}

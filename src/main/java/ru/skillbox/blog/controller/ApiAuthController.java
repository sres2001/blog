package ru.skillbox.blog.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog.api.request.LoginRequest;
import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.api.request.RestorePasswordRequest;
import ru.skillbox.blog.api.response.AuthCheckResponse;
import ru.skillbox.blog.api.response.BaseResponse;
import ru.skillbox.blog.api.response.CaptchaResponse;
import ru.skillbox.blog.api.response.LoginResponse;
import ru.skillbox.blog.dto.UserProfileDto;
import ru.skillbox.blog.dto.mapper.RequestMapper;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.AuthService;
import ru.skillbox.blog.service.MailService;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
public class ApiAuthController {

    private final AuthService authService;
    private final MailService mailService;

    public ApiAuthController(AuthService authService, MailService mailService) {
        this.authService = authService;
        this.mailService = mailService;
    }

    @GetMapping("check")
    public AuthCheckResponse check(Principal principal) {
        if (principal != null) {
            UserProfileDto user = authService.getUserProfile(principal.getName());
            return ResponseMapper.toCheckResponse(user);
        } else {
            return new AuthCheckResponse(false);
        }
    }

    @GetMapping("captcha")
    public CaptchaResponse captcha() {
        return ResponseMapper.toCaptchaResponse(authService.createCaptcha());
    }

    @PostMapping("register")
    public BaseResponse register(@RequestBody RegisterRequest data) {
        authService.registerUser(RequestMapper.toRegisterDto(data));
        return BaseResponse.success();
    }

    @PostMapping("login")
    public LoginResponse login(@RequestBody LoginRequest data) {
        try {
            UserProfileDto user = authService.authenticateUser(data.getEmail(), data.getPassword());
            return ResponseMapper.toLoginResponse(user);
        } catch (AuthenticationException e) {
            return new LoginResponse(false);
        }
    }

    @GetMapping("logout")
    public BaseResponse logout(Principal principal) {
        if (principal != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return BaseResponse.success();
    }

    @PostMapping("restore")
    public BaseResponse restore(@RequestBody RestorePasswordRequest request) {
        String code = authService.restorePassword(request.getEmail());
        mailService.sendPasswordRestoreEmail(request.getEmail(), code);
        return BaseResponse.success();
    }
}

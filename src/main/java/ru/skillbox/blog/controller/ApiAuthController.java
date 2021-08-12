package ru.skillbox.blog.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog.api.request.LoginRequest;
import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.api.response.*;
import ru.skillbox.blog.dto.UserProfileDto;
import ru.skillbox.blog.dto.mapper.RequestMapper;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.AuthService;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
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
    public RegisterResponse register(@RequestBody RegisterRequest data) {
        return ResponseMapper.toRegisterResponse(
                authService.registerUser(
                        RequestMapper.toRegisterDto(data)));
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
    public LogoutResponse logout(Principal principal) {
        if (principal != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return new LogoutResponse(true);
    }
}

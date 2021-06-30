package ru.skillbox.blog.controller;

import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog.api.request.RegisterRequest;
import ru.skillbox.blog.api.response.AuthCheckResponse;
import ru.skillbox.blog.api.response.CaptchaResponse;
import ru.skillbox.blog.api.response.RegisterResponse;
import ru.skillbox.blog.dto.mapper.RequestMapper;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.AuthService;

@RestController
@RequestMapping("api/auth")
public class ApiAuthController {

    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("check")
    public AuthCheckResponse check() {
        return new AuthCheckResponse(false);
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
}

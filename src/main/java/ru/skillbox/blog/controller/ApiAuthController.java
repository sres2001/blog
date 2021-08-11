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
import ru.skillbox.blog.service.PostService;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
public class ApiAuthController {

    private final AuthService authService;
    private final PostService postService;

    public ApiAuthController(AuthService authService, PostService postService) {
        this.authService = authService;
        this.postService = postService;
    }

    @GetMapping("check")
    public AuthCheckResponse check(Principal principal) {
        if (principal != null) {
            UserProfileDto user = authService.getUser(principal.getName());
            long moderationCount = user.isModerator() ? postService.countPostsForModeration() : 0;
            return ResponseMapper.toCheckResponse(user, moderationCount);
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
            long moderationCount = user.isModerator() ? postService.countPostsForModeration() : 0;
            return ResponseMapper.toLoginResponse(user, moderationCount);
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

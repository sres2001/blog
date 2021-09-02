package ru.skillbox.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog.api.request.ChangePasswordRequest;
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
import ru.skillbox.blog.exceptions.ApiException;
import ru.skillbox.blog.service.AuthService;
import ru.skillbox.blog.service.GlobalSettingService;
import ru.skillbox.blog.service.MailService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.StringJoiner;

@RestController
@RequestMapping("api/auth")
public class ApiAuthController {

    private final AuthService authService;
    private final MailService mailService;
    private final GlobalSettingService globalSettingService;

    public ApiAuthController(
            AuthService authService,
            MailService mailService,
            GlobalSettingService globalSettingService
    ) {
        this.authService = authService;
        this.mailService = mailService;
        this.globalSettingService = globalSettingService;
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
    public BaseResponse register(@RequestBody RegisterRequest request) {
        if (!globalSettingService.isMultiuserMode()) {
            throw new ApiException(HttpStatus.NOT_FOUND, null);
        }
        authService.registerUser(RequestMapper.toRegisterDto(request));
        return BaseResponse.success();
    }

    @PostMapping("login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            UserProfileDto user = authService.authenticateUser(request.getEmail(), request.getPassword());
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
    public BaseResponse restore(HttpServletRequest httpRequest, @RequestBody RestorePasswordRequest request) {
        String code = authService.restorePassword(request.getEmail());
        mailService.sendPasswordRestoreEmail(getApplicationUrl(httpRequest), request.getEmail(), code);
        return BaseResponse.success();
    }

    private String getApplicationUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        StringJoiner stringJoiner = new StringJoiner("");
        stringJoiner.add(scheme);
        stringJoiner.add("://");
        stringJoiner.add(request.getServerName());
        if (scheme.equals("http") && request.getServerPort() != 80 ||
                scheme.equals("https") && request.getServerPort() != 443) {
            stringJoiner.add(":");
            stringJoiner.add(Integer.toString(request.getServerPort()));
        }
        return stringJoiner.toString();
    }

    @PostMapping("password")
    public BaseResponse password(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(RequestMapper.toChangePasswordDto(request));
        return BaseResponse.success();
    }
}

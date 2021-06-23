package ru.skillbox.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.response.AuthCheckResponse;

@RestController
@RequestMapping("api/auth")
public class ApiAuthController {

    @GetMapping("check")
    public AuthCheckResponse check() {
        return new AuthCheckResponse(false);
    }
}

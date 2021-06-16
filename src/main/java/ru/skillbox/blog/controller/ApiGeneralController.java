package ru.skillbox.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.response.InitResponse;
import ru.skillbox.blog.api.response.SettingsResponse;
import ru.skillbox.blog.service.BlogInformation;
import ru.skillbox.blog.dto.mapper.ResponseMapper;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;

    public ApiGeneralController(BlogInformation blogInformation) {
        this.initResponse = ResponseMapper.toInitResponse(blogInformation);
    }

    @GetMapping("init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("settings")
    public SettingsResponse settings() {
        //TODO:
        return new SettingsResponse();
    }
}

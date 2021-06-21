package ru.skillbox.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.response.InitResponse;
import ru.skillbox.blog.api.response.SettingsResponse;
import ru.skillbox.blog.api.response.TagListResponse;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.BlogInformation;
import ru.skillbox.blog.service.GlobalSettingService;

import java.util.List;

@RestController
@RequestMapping("api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final GlobalSettingService globalSettingsService;

    public ApiGeneralController(BlogInformation blogInformation, GlobalSettingService globalSettingsService) {
        this.initResponse = ResponseMapper.toInitResponse(blogInformation);
        this.globalSettingsService = globalSettingsService;
    }

    @GetMapping("init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("settings")
    public SettingsResponse settings() {
        SettingsResponse response = new SettingsResponse();
        response.setMultiuserMode(globalSettingsService.getBoolean("MULTIUSER_MODE"));
        response.setPostPremoderation(globalSettingsService.getBoolean("POST_PREMODERATION"));
        response.setStatisticsIsPublic(globalSettingsService.getBoolean("STATISTICS_IS_PUBLIC"));
        return response;
    }

    @GetMapping("tag")
    public TagListResponse tags(@RequestParam(required = false) String query) {
        return new TagListResponse(List.of());
    }
}

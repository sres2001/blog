package ru.skillbox.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.response.CalendarResponse;
import ru.skillbox.blog.api.response.InitResponse;
import ru.skillbox.blog.api.response.SettingsResponse;
import ru.skillbox.blog.api.response.TagListResponse;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.BlogInformation;
import ru.skillbox.blog.service.GlobalSettingService;
import ru.skillbox.blog.service.PostService;
import ru.skillbox.blog.service.TagService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final GlobalSettingService globalSettingsService;
    private final TagService tagService;
    private final PostService postService;

    public ApiGeneralController(
            BlogInformation blogInformation,
            GlobalSettingService globalSettingsService,
            TagService tagService,
            PostService postService
    ) {
        this.initResponse = ResponseMapper.toInitResponse(blogInformation);
        this.globalSettingsService = globalSettingsService;
        this.tagService = tagService;
        this.postService = postService;
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
        List<TagDto> tags = tagService.listTagsWithWeights(query);
        return ResponseMapper.toTagListResponse(tags);
    }

    @GetMapping("calendar")
    public CalendarResponse calendar(@RequestParam(required = false) @Min(1111) @Max(9999) Integer year) {
        CalendarDto calendar = postService.getPostsCalendarByYear(Optional.ofNullable(year)
                .orElse(LocalDate.now(ZoneOffset.UTC).getYear()));
        return ResponseMapper.toCalendarResponse(calendar);
    }
}

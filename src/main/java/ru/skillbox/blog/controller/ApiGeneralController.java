package ru.skillbox.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.api.response.CalendarResponse;
import ru.skillbox.blog.api.response.InitResponse;
import ru.skillbox.blog.api.response.SettingsResponse;
import ru.skillbox.blog.api.response.TagListResponse;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.*;

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
    private final FileStorageService fileStorageService;

    public ApiGeneralController(
            BlogInformation blogInformation,
            GlobalSettingService globalSettingsService,
            TagService tagService,
            PostService postService,
            FileStorageService fileStorageService
    ) {
        this.initResponse = ResponseMapper.toInitResponse(blogInformation);
        this.globalSettingsService = globalSettingsService;
        this.tagService = tagService;
        this.postService = postService;
        this.fileStorageService = fileStorageService;
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

    @PostMapping("image")
    @PreAuthorize("hasAnyAuthority('post:write', 'post:moderate')")
    public ResponseEntity<?> saveImage(@RequestParam MultipartFile image) {
        return ResponseEntity.ok(fileStorageService.saveImage(image));
    }
}

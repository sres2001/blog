package ru.skillbox.blog.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.api.request.CommentRequest;
import ru.skillbox.blog.api.request.ModerationRequest;
import ru.skillbox.blog.api.request.UpdateProfileRequest;
import ru.skillbox.blog.api.request.UpdateProfileWithPhotoRequest;
import ru.skillbox.blog.api.response.*;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.dto.mapper.RequestMapper;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
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
    private final AuthService authService;

    public ApiGeneralController(
            BlogInformation blogInformation,
            GlobalSettingService globalSettingsService,
            TagService tagService,
            PostService postService,
            FileStorageService fileStorageService,
            AuthService authService
    ) {
        this.initResponse = ResponseMapper.toInitResponse(blogInformation);
        this.globalSettingsService = globalSettingsService;
        this.tagService = tagService;
        this.postService = postService;
        this.fileStorageService = fileStorageService;
        this.authService = authService;
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
        return ResponseEntity.ok(fileStorageService.saveUploadedImage(image));
    }

    @PostMapping("comment")
    @PreAuthorize("hasAnyAuthority('post:write', 'post:moderate')")
    public IdResponse comment(Principal principal, @RequestBody CommentRequest request) {
        int userId = authService.getUser(principal.getName()).getId();
        int commentId = postService.addComment(userId, request.getPostId(), request.getParentId(), request.getText());
        return new IdResponse(commentId);
    }

    @PostMapping("moderation")
    @PreAuthorize("hasAuthority('post:moderate')")
    public BaseResponse moderation(Principal principal, @RequestBody ModerationRequest request) {
        int moderatorId = authService.getUser(principal.getName()).getId();
        postService.moderatePost(moderatorId, request.getPostId(), request.getDecision());
        return BaseResponse.success();
    }

    @PostMapping(value = "profile/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public BaseResponse updateProfileFromMultipartFormData(
            Principal principal,
            @ModelAttribute UpdateProfileWithPhotoRequest request
    ) {
        return updateProfile(principal, request, request.getPhoto());
    }

    @PostMapping(value = "profile/my")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse updateProfileFromJson(Principal principal, @RequestBody UpdateProfileRequest request) {
        return updateProfile(principal, request, null);
    }

    private BaseResponse updateProfile(Principal principal, UpdateProfileRequest request, MultipartFile photo) {
        int userId = authService.getUser(principal.getName()).getId();
        authService.updateProfile(RequestMapper.toUpdateProfileDto(userId, request, photo));
        return BaseResponse.success();
    }
}

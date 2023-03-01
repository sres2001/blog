package ru.skillbox.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog.api.request.CommentRequest;
import ru.skillbox.blog.api.request.ModerationRequest;
import ru.skillbox.blog.api.request.UpdateProfileRequest;
import ru.skillbox.blog.api.request.UpdateProfileWithPhotoRequest;
import ru.skillbox.blog.api.response.*;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.SettingsDto;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.dto.mapper.RequestMapper;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.*;

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
        return ResponseMapper.toSettingsResponse(globalSettingsService.getSettings());
    }

    @PutMapping("settings")
    @PreAuthorize("hasAuthority('settings:write')")
    public void putSettings(Principal principal, @RequestBody SettingsResponse request) {
        SettingsDto dto = RequestMapper.toSettingsDto(request);
        globalSettingsService.updateSettings(dto);
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
            HttpServletRequest request,
            Principal principal,
            @ModelAttribute UpdateProfileWithPhotoRequest apiRequest
    ) {
        return updateProfile(request, principal, apiRequest, apiRequest.getPhoto());
    }

    @PostMapping(value = "profile/my")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse updateProfileFromJson(
            HttpServletRequest request,
            Principal principal,
            @RequestBody UpdateProfileRequest apiRequest) {
        return updateProfile(request, principal, apiRequest, null);
    }

    private BaseResponse updateProfile(
            HttpServletRequest request,
            Principal principal,
            UpdateProfileRequest apiRequest,
            MultipartFile photo) {
        int userId = authService.getUser(principal.getName()).getId();
        authService.updateProfile(request, RequestMapper.toUpdateProfileDto(userId, apiRequest, photo));
        return BaseResponse.success();
    }

    @GetMapping("statistics/my")
    @PreAuthorize("hasAuthority('post:write')")
    public StatisticsResponse myStatistics(Principal principal) {
        int userId = authService.getUser(principal.getName()).getId();
        return ResponseMapper.toStatisticsResponse(postService.getStatisticsByUser(userId));
    }

    @GetMapping("statistics/all")
    public StatisticsResponse allStatistics(Principal principal) {
        if (!globalSettingsService.isStatisticsPublic()) {
            if (principal == null || !authService.getUser(principal.getName()).isModerator()) {
                throw new AccessDeniedException("Статистика доступна только модераторам.");
            }
        }
        return ResponseMapper.toStatisticsResponse(postService.getAllStatistics());
    }
}

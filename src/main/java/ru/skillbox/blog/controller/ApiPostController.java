package ru.skillbox.blog.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.blog.api.request.*;
import ru.skillbox.blog.api.response.BaseResponse;
import ru.skillbox.blog.api.response.PostListResponse;
import ru.skillbox.blog.api.response.PostResponse;
import ru.skillbox.blog.dto.PostDto;
import ru.skillbox.blog.dto.UserDto;
import ru.skillbox.blog.dto.mapper.RequestMapper;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.exceptions.EntityNotFoundException;
import ru.skillbox.blog.service.AuthService;
import ru.skillbox.blog.service.PostService;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("api/post")
public class ApiPostController {

    private final PostService postService;
    private final AuthService authService;

    public ApiPostController(PostService postService, AuthService authService) {
        this.postService = postService;
        this.authService = authService;
    }

    @GetMapping
    public PostListResponse getPosts(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "recent") PostListMode mode
    ) {
        return ResponseMapper.toPostListResponse(
                postService.getPosts(offset, limit, mode));
    }

    @GetMapping("{id}")
    public PostResponse getPost(Principal principal, @PathVariable int id) {
        Integer viewerId = null;
        boolean viewerIsModerator = false;
        if (principal != null) {
            UserDto user = authService.getUser(principal.getName());
            viewerId = user.getId();
            viewerIsModerator = user.isModerator();
        }
        PostDto post = postService.findPostById(id, viewerId, viewerIsModerator)
                .orElseThrow(EntityNotFoundException::new);
        return ResponseMapper.toPostResponse(post);
    }

    @GetMapping("search")
    public PostListResponse searchPosts(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) String query
    ) {
        if (query == null || query.isBlank()) {
            return getPosts(offset, limit, PostListMode.RECENT);
        }
        return ResponseMapper.toPostListResponse(
                postService.searchPosts(offset, limit, query));
    }

    @GetMapping("byDate")
    public PostListResponse getPostsByDate(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseMapper.toPostListResponse(
                postService.getPostsByDate(offset, limit, date));
    }

    @GetMapping("byTag")
    public PostListResponse getPostsByTag(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam String tag
    ) {
        return ResponseMapper.toPostListResponse(
                postService.getPostsByTag(offset, limit, tag));
    }

    @GetMapping("my")
    @PreAuthorize("hasAuthority('post:write')")
    public PostListResponse getMyPosts(
            Principal principal,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "inactive") MyPostListStatus status
    ) {
        int userId = authService.getUser(principal.getName()).getId();
        return ResponseMapper.toPostListResponse(
                postService.getUserPosts(userId, offset, limit, status));
    }

    @GetMapping("moderation")
    @PreAuthorize("hasAuthority('post:moderate')")
    public PostListResponse getModeratorPosts(
            Principal principal,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "new") ModeratorPostListStatus status
    ) {
        int moderatorId = authService.getUser(principal.getName()).getId();
        return ResponseMapper.toPostListResponse(
                postService.getModeratorPosts(moderatorId, offset, limit, status));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('post:write')")
    public BaseResponse addPost(Principal principal, @RequestBody EditPostRequest request) {
        UserDto user = authService.getUser(principal.getName());
        postService.addPost(RequestMapper.toEditPostDto(user.getId(), request));
        return BaseResponse.success();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('post:write', 'post:moderate')")
    public BaseResponse editPost(Principal principal, @PathVariable int id, @RequestBody EditPostRequest request) {
        UserDto user = authService.getUser(principal.getName());
        postService.editPost(id, RequestMapper.toEditPostDto(user.getId(), request));
        return BaseResponse.success();
    }

    @PostMapping("like")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse like(Principal principal, @RequestBody LikeRequest request) {
        UserDto user = authService.getUser(principal.getName());
        boolean set = postService.setLike(user.getId(), request.getPostId());
        return new BaseResponse(set);
    }

    @PostMapping("dislike")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse dislike(Principal principal, @RequestBody LikeRequest request) {
        UserDto user = authService.getUser(principal.getName());
        boolean set = postService.setDislike(user.getId(), request.getPostId());
        return new BaseResponse(set);
    }
}

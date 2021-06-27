package ru.skillbox.blog.controller;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.api.response.PostListResponse;
import ru.skillbox.blog.dto.mapper.ResponseMapper;
import ru.skillbox.blog.service.PostService;

@RestController
@RequestMapping("api/post")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
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
}

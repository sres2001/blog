package ru.skillbox.blog.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.api.response.PostListResponse;
import ru.skillbox.blog.dto.PostListItemDto;
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
        Page<PostListItemDto> listDto = postService.getPosts(offset, limit, mode);
        return ResponseMapper.toPostListResponse(listDto);
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
        Page<PostListItemDto> listDto = postService.searchPosts(offset, limit, query);
        return ResponseMapper.toPostListResponse(listDto);
    }
}

package ru.skillbox.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.blog.api.response.PostListResponse;
import ru.skillbox.blog.api.response.TagListResponse;
import ru.skillbox.blog.service.PostService;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostListResponse posts(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "recent") String mode
    ) {
        //TODO:
        switch (mode) {
            case "popular":
                break;
            case "best":
                break;
            case "early":
                break;
            case "recent":
            default:
                break;
        }
        return new PostListResponse(0, List.of());
    }

    @GetMapping("tag")
    public TagListResponse tags(@RequestParam(required = false) String query) {
        return new TagListResponse(List.of());
    }
}

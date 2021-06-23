package ru.skillbox.blog.service;

import org.springframework.data.domain.Page;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.PostListItemDto;

public interface PostService {
    Page<PostListItemDto> getPosts(int offset, int limit, PostListMode mode);
}

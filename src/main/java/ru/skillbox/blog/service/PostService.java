package ru.skillbox.blog.service;

import org.springframework.data.domain.Page;
import ru.skillbox.blog.api.request.MyPostListStatus;
import ru.skillbox.blog.api.request.ModeratorPostListStatus;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.PostDto;
import ru.skillbox.blog.dto.PostListItemDto;

import java.time.LocalDate;
import java.util.Optional;

public interface PostService {

    Optional<PostDto> findPostById(int postId, Integer viewerId, boolean viewerIsModerator);

    Page<PostListItemDto> getPosts(int offset, int limit, PostListMode mode);

    Page<PostListItemDto> searchPosts(int offset, int limit, String query);

    CalendarDto getPostsCalendarByYear(int year);

    Page<PostListItemDto> getPostsByDate(int offset, int limit, LocalDate date);

    Page<PostListItemDto> getPostsByTag(int offset, int limit, String tag);

    Page<PostListItemDto> getUserPosts(int userId, int offset, int limit, MyPostListStatus status);

    long getModerationCountByAuthor(int userId);

    Page<PostListItemDto> getModeratorPosts(int moderatorId, int offset, int limit, ModeratorPostListStatus status);
}

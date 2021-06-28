package ru.skillbox.blog.dto.mapper;

import org.springframework.data.domain.Page;
import ru.skillbox.blog.dto.*;
import ru.skillbox.blog.model.PostComment;
import ru.skillbox.blog.model.PostListItem;
import ru.skillbox.blog.model.Tag;
import ru.skillbox.blog.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static Page<PostListItemDto> toPostListDto(Page<PostListItem> postsPage) {
        return postsPage.map(DtoMapper::toPostListDto);
    }

    public static PostListItemDto toPostListDto(PostListItem item) {
        PostListItemDto dto = new PostListItemDto();
        dto.setId(item.getId());
        dto.setTimestampAsEpochSeconds(item.getTime().toInstant().getEpochSecond());
        dto.setUser(toUserDto(item.getUser()));
        dto.setTitle(item.getTitle());
        dto.setAnnounce(toAnnounce(item.getText()));
        dto.setLikeCount(item.getLikeCount());
        dto.setDislikeCount(item.getDislikeCount());
        dto.setCommentCount(item.getCommentCount());
        dto.setViewCount(item.getViewCount());
        return dto;
    }

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    private static CommentAuthorDto toCommentAuthorDto(User user) {
        CommentAuthorDto dto = new CommentAuthorDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhoto(user.getPhoto());
        return dto;
    }

    private static String toAnnounce(String html) {
        String text = html.replaceAll("<.*?>", "");
        if (text.length() > 150) {
            return text.substring(0, 147) + "...";
        }
        return text;
    }

    public static PostDto toPostDto(PostListItem post, boolean active, List<PostComment> comments, List<Tag> tags) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTimestampAsEpochSeconds(post.getTime().toInstant().getEpochSecond());
        dto.setActive(active);
        dto.setUser(toUserDto(post.getUser()));
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setLikeCount(post.getLikeCount());
        dto.setDislikeCount(post.getDislikeCount());
        dto.setViewCount(post.getViewCount());
        dto.setComments(comments.stream()
                .map(DtoMapper::toCommentDto)
                .collect(Collectors.toList()));
        dto.setTags(tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        return dto;
    }

    private static CommentDto toCommentDto(PostComment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setTimestampAsEpochSeconds(comment.getTime().toInstant().getEpochSecond());
        dto.setText(comment.getText());
        dto.setUser(toCommentAuthorDto(comment.getUser()));
        return dto;
    }
}

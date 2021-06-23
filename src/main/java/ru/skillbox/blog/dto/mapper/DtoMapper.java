package ru.skillbox.blog.dto.mapper;

import org.springframework.data.domain.Page;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.UserDto;
import ru.skillbox.blog.model.PostListItem;
import ru.skillbox.blog.model.User;

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

    private static String toAnnounce(String html) {
        String text = html.replaceAll("<.*?>", "");
        if (text.length() > 150) {
            return text.substring(0, 147) + "...";
        }
        return text;
    }
}

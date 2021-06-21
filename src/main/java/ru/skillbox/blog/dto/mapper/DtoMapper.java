package ru.skillbox.blog.dto.mapper;

import org.jsoup.Jsoup;
import org.springframework.data.domain.Page;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.UserDto;
import ru.skillbox.blog.model.PostView;
import ru.skillbox.blog.model.User;

public class DtoMapper {

    public static Page<PostListItemDto> toPostListDto(Page<PostView> postsPage) {
        return postsPage.map(DtoMapper::toPostListDto);
    }

    public static PostListItemDto toPostListDto(PostView postView) {
        PostListItemDto dto = new PostListItemDto();
        dto.setId(postView.getId());
        dto.setTimestamp(postView.getTime().toInstant().getEpochSecond());
        dto.setUser(toUserDto(postView.getUser()));
        dto.setTitle(postView.getTitle());
        dto.setAnnounce(toAnnounce(postView.getText()));
        dto.setLikeCount(postView.getLikeCount());
        dto.setDislikeCount(postView.getDislikeCount());
        dto.setCommentCount(postView.getCommentCount());
        dto.setViewCount(postView.getViewCount());
        return dto;
    }

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    private static String toAnnounce(String html) {
        String text = Jsoup.parse(html).text();
        if (text.length() > 150) {
            return text.substring(0, 147) + "...";
        }
        return text;
    }
}

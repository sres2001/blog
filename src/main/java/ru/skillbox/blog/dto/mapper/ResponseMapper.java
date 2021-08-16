package ru.skillbox.blog.dto.mapper;

import org.springframework.data.domain.Page;
import ru.skillbox.blog.api.response.*;
import ru.skillbox.blog.dto.*;
import ru.skillbox.blog.service.BlogInformation;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseMapper {

    public static InitResponse toInitResponse(BlogInformation info) {
        InitResponse response = new InitResponse();
        response.setTitle(info.getTitle());
        response.setSubtitle(info.getSubtitle());
        response.setPhone(info.getPhone());
        response.setEmail(info.getEmail());
        response.setCopyright(info.getCopyright());
        response.setCopyrightFrom(info.getCopyrightFrom());
        return response;
    }

    public static PostListResponse toPostListResponse(Page<PostListItemDto> listDto) {
        return new PostListResponse(listDto.getTotalElements(), listDto.stream()
                .map(ResponseMapper::toPostListItemResponse)
                .collect(Collectors.toList()));
    }

    public static PostListItemResponse toPostListItemResponse(PostListItemDto dto) {
        PostListItemResponse response = new PostListItemResponse();
        response.setId(dto.getId());
        response.setTimestampAsEpochSeconds(dto.getTimestampAsEpochSeconds());
        response.setUser(toUserResponse(dto.getUser()));
        response.setTitle(dto.getTitle());
        response.setAnnounce(dto.getAnnounce());
        response.setLikeCount(dto.getLikeCount());
        response.setDislikeCount(dto.getDislikeCount());
        response.setCommentCount(dto.getCommentCount());
        response.setViewCount(dto.getViewCount());
        return response;
    }

    public static UserResponse toUserResponse(UserDto dto) {
        UserResponse response = new UserResponse();
        response.setId(dto.getId());
        response.setName(dto.getName());
        return response;
    }

    public static TagListResponse toTagListResponse(List<TagDto> tags) {
        return new TagListResponse(tags.stream()
                .map(ResponseMapper::toTagResponse)
                .collect(Collectors.toList()));
    }

    private static TagResponse toTagResponse(TagDto dto) {
        TagResponse response = new TagResponse();
        response.setName(dto.getName());
        response.setWeight(dto.getWeight());
        return response;
    }

    public static CalendarResponse toCalendarResponse(CalendarDto dto) {
        CalendarResponse response = new CalendarResponse();
        response.setYears(dto.getYears());
        if (dto.getPosts().isEmpty()) {
            response.setPosts(Map.of());
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            response.setPosts(dto.getPosts().entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> formatter.format(e.getKey()),
                            Map.Entry::getValue)));
        }
        return response;
    }

    public static PostResponse toPostResponse(PostDto dto) {
        PostResponse response = new PostResponse();
        response.setId(dto.getId());
        response.setTimestampAsEpochSeconds(dto.getTimestampAsEpochSeconds());
        response.setActive(dto.isActive());
        response.setUser(toUserResponse(dto.getUser()));
        response.setTitle(dto.getTitle());
        response.setText(dto.getText());
        response.setLikeCount(dto.getLikeCount());
        response.setDislikeCount(dto.getDislikeCount());
        response.setViewCount(dto.getViewCount());
        response.setComments(dto.getComments().stream()
                .map(ResponseMapper::toCommentResponse)
                .collect(Collectors.toList()));
        response.setTags(dto.getTags());
        return response;
    }

    private static CommentResponse toCommentResponse(CommentDto dto) {
        CommentResponse response = new CommentResponse();
        response.setId(dto.getId());
        response.setParentId(dto.getParentId());
        response.setTimestampAsEpochSeconds(dto.getTimestampAsEpochSeconds());
        response.setText(dto.getText());
        response.setUser(toCommentAuthorResponse(dto.getUser()));
        return response;
    }

    private static CommentAuthorResponse toCommentAuthorResponse(CommentAuthorDto dto) {
        CommentAuthorResponse response = new CommentAuthorResponse();
        response.setId(dto.getId());
        response.setName(dto.getName());
        response.setPhoto(dto.getPhoto());
        return response;
    }

    public static CaptchaResponse toCaptchaResponse(CaptchaDto dto) {
        CaptchaResponse response = new CaptchaResponse();
        response.setSecret(dto.getSecret());
        response.setImage("data:image/png;base64, " + Base64.getEncoder().encodeToString(dto.getImage()));
        return response;
    }

    public static LoginResponse toLoginResponse(UserProfileDto dto) {
        LoginResponse response = new LoginResponse(true);
        response.setUser(toUserProfileResponse(dto));
        return response;
    }

    private static UserProfileResponse toUserProfileResponse(UserProfileDto dto) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(dto.getId());
        response.setName(dto.getName());
        response.setPhoto(dto.getPhoto());
        response.setEmail(dto.getEmail());
        if (dto.isModerator()) {
            response.setModeration(true);
            response.setSettings(true);
            response.setModerationCount(dto.getModerationCount());
        }
        return response;
    }

    public static AuthCheckResponse toCheckResponse(UserProfileDto dto) {
        AuthCheckResponse response = new AuthCheckResponse(true);
        response.setUser(toUserProfileResponse(dto));
        return response;
    }
}

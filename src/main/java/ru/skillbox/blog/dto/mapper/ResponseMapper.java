package ru.skillbox.blog.dto.mapper;

import org.springframework.data.domain.Page;
import ru.skillbox.blog.api.response.*;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.dto.UserDto;
import ru.skillbox.blog.service.BlogInformation;

import java.util.List;
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
}

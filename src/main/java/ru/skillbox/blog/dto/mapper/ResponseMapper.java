package ru.skillbox.blog.dto.mapper;

import ru.skillbox.blog.api.response.InitResponse;
import ru.skillbox.blog.service.BlogInformation;

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
}

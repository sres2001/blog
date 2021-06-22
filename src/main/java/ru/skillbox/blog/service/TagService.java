package ru.skillbox.blog.service;

import ru.skillbox.blog.dto.TagDto;

import java.util.List;

public interface TagService {
    List<TagDto> listTagsWithWeights(String query);
}

package ru.skillbox.blog.repository;

import java.util.List;

public interface TagRepositoryCustom {
    List<TagAndCount> getTagsAndPostsCounts(String query);
}

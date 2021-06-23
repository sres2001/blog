package ru.skillbox.blog.repository;

import java.util.List;

public interface TagRepositoryCustom {
    List<TagAndCount> getTagsAndWeights(String query);
}

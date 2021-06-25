package ru.skillbox.blog.service.impl;

import org.springframework.stereotype.Service;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.repository.PostRepository;
import ru.skillbox.blog.repository.TagAndCount;
import ru.skillbox.blog.repository.TagRepository;
import ru.skillbox.blog.service.TagService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<TagDto> listTagsWithWeights(String query)
    {
        List<TagAndCount> queryResults = tagRepository.getTagsAndPostsCounts(query);
        if (queryResults.isEmpty()) {
            return List.of();
        }
        TagAndCount mostPopular = queryResults.stream()
                .max(Comparator.comparing(TagAndCount::getPostsCount))
                .get();
        long allPostsCount = postRepository.countAllPublished();
        double maxWeight = (double) mostPopular.getPostsCount() / allPostsCount;
        double k = 1 / maxWeight;
        return queryResults.stream()
                .map(result -> new TagDto(result.getName(), k * result.getPostsCount() / allPostsCount))
                .collect(Collectors.toList());
    }
}

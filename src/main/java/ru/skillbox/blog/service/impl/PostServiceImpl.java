package ru.skillbox.blog.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.model.PostListItem;
import ru.skillbox.blog.repository.PostListItemRepository;
import ru.skillbox.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    private final PostListItemRepository repository;

    public PostServiceImpl(PostListItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<PostListItemDto> getPosts(int offset, int limit, PostListMode mode) {
        Pageable pageable;
        switch (mode) {
            case POPULAR:
                pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "commentCount"));
                break;
            case BEST:
                pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "likeCount"));
                break;
            case EARLY:
                pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "time"));
                break;
            case RECENT:
            default:
                pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "time"));
                break;
        }
        Page<PostListItem> page = repository.findAll(pageable);
        return DtoMapper.toPostListDto(page);
    }
}

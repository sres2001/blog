package ru.skillbox.blog.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.model.ModerationStatus;
import ru.skillbox.blog.model.PostListItem;
import ru.skillbox.blog.repository.PostListItemRepository;
import ru.skillbox.blog.service.PostService;

import java.util.Date;

@Service
public class PostServiceImpl implements PostService {

    private final PostListItemRepository repository;

    public PostServiceImpl(PostListItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<PostListItemDto> getPosts(int offset, int limit, PostListMode mode) {
        Pageable pageable;
        int pageNumber = offset / limit;
        switch (mode) {
            case POPULAR:
                pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "commentCount"));
                break;
            case BEST:
                pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "likeCount"));
                break;
            case EARLY:
                pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.ASC, "time"));
                break;
            case RECENT:
            default:
                pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "time"));
                break;
        }
        Page<PostListItem> page = repository.findByActiveAndModerationStatusAndTimeLessThanEqual(
                (byte)1,
                ModerationStatus.ACCEPTED,
                new Date(),
                pageable);
        return DtoMapper.toPostListDto(page);
    }
}

package ru.skillbox.blog.service.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.model.PostListItem;
import ru.skillbox.blog.model.Tag;
import ru.skillbox.blog.repository.DateAndCount;
import ru.skillbox.blog.repository.PostListItemRepository;
import ru.skillbox.blog.repository.PostRepository;
import ru.skillbox.blog.repository.TagRepository;
import ru.skillbox.blog.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostListItemRepository viewRepository;
    private final PostRepository entityRepository;
    private final TagRepository tagRepository;

    public PostServiceImpl(
            PostListItemRepository viewRepository,
            PostRepository entityRepository,
            TagRepository tagRepository) {
        this.viewRepository = viewRepository;
        this.entityRepository = entityRepository;
        this.tagRepository = tagRepository;
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
        return DtoMapper.toPostListDto(viewRepository.findPublished(pageable));
    }

    @Override
    public Page<PostListItemDto> searchPosts(int offset, int limit, String query) {
        return DtoMapper.toPostListDto(
                viewRepository.searchPublished(
                        query,
                        recent(offset, limit)));
    }

    private PageRequest recent(int offset, int limit) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "time"));
    }

    @Override
    public CalendarDto getPostsCalendarByYear(int year) {
        List<Integer> years = entityRepository.getPublishedPostsYears();
        List<DateAndCount> datesAndCount = entityRepository.getPublishedPostsCountsByDatesInYear(year);
        CalendarDto calendar = new CalendarDto();
        calendar.setYears(years);
        calendar.setPosts(datesAndCount.stream()
                .collect(Collectors.toMap(DateAndCount::getDate, DateAndCount::getPostsCount)));
        return calendar;
    }

    @Override
    public Page<PostListItemDto> getPostsByDate(int offset, int limit, LocalDate date) {
        Page<PostListItem> page = viewRepository.findPublishedByDate(
                Date.from(date.atStartOfDay().toInstant(ZoneOffset.UTC)),
                recent(offset, limit));
        return DtoMapper.toPostListDto(page);
    }

    @Override
    public Page<PostListItemDto> getPostsByTag(int offset, int limit, String tag) {
        Optional<Tag> tagOptional = tagRepository.findOneByNameIgnoreCase(tag);
        if (tagOptional.isEmpty()) {
            return Page.empty();
        }
        Page<PostListItem> page = viewRepository.findPublishedByTag(tagOptional.get(),
                recent(offset, limit));
        return DtoMapper.toPostListDto(page);
    }
}

package ru.skillbox.blog.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.api.request.ModeratorPostListStatus;
import ru.skillbox.blog.api.request.MyPostListStatus;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.EditPostRequestDto;
import ru.skillbox.blog.dto.PostDto;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.dto.mapper.BaseResponseDto;
import ru.skillbox.blog.dto.mapper.DtoMapper;
import ru.skillbox.blog.model.*;
import ru.skillbox.blog.repository.*;
import ru.skillbox.blog.service.GlobalSettingService;
import ru.skillbox.blog.service.PostService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostListItemRepository viewRepository;
    private final PostRepository entityRepository;
    private final TagRepository tagRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostTagRepository postTagRepository;
    private final GlobalSettingService globalSettingService;

    public PostServiceImpl(
            PostListItemRepository viewRepository,
            PostRepository entityRepository,
            TagRepository tagRepository,
            PostCommentRepository postCommentRepository,
            UserRepository userRepository,
            PostTagRepository postTagRepository,
            GlobalSettingService globalSettingService) {
        this.viewRepository = viewRepository;
        this.entityRepository = entityRepository;
        this.tagRepository = tagRepository;
        this.postCommentRepository = postCommentRepository;
        this.userRepository = userRepository;
        this.postTagRepository = postTagRepository;
        this.globalSettingService = globalSettingService;
    }

    @Override
    @Transactional
    public Optional<PostDto> findPostById(int postId, Integer viewerId, boolean viewerIsModerator) {
        Optional<PostListItem> postOptional = viewRepository.findById(postId);
        if (postOptional.isPresent()) {
            PostListItem post = postOptional.get();

            boolean postIsActive = post.getActive() == 1 &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED &&
                    post.getTime().before(new Date());

            boolean viewerIsAuthor = viewerId != null && post.getUser().getId() == viewerId;
            boolean specialViewer = viewerIsModerator || viewerIsAuthor;

            if (specialViewer || postIsActive) {
                if (!specialViewer) {
                    entityRepository.incrementViewCount(postId);
                }
                List<PostComment> comments = postCommentRepository.findByPostId(postId);
                List<Tag> tags = tagRepository.findPostTags(postId);
                return Optional.of(DtoMapper.toPostDto(post, postIsActive, comments, tags));
            }
        }
        return Optional.empty();
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

    @Override
    public Page<PostListItemDto> getUserPosts(int userId, int offset, int limit, MyPostListStatus status) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.ASC, "time"));
        if (status == MyPostListStatus.INACTIVE) {
            return DtoMapper.toPostListDto(viewRepository.findByUserIdAndActive(userId, (byte) 0, pageable));
        }
        return DtoMapper.toPostListDto(
                viewRepository.findByUserIdAndActiveAndModerationStatus(userId, (byte)1,
                        getModerationStatusForQuery(status), pageable));
    }

    private ModerationStatus getModerationStatusForQuery(MyPostListStatus status) {
        switch (status) {
            case PENDING:
                return ModerationStatus.NEW;
            case DECLINED:
                return ModerationStatus.DECLINED;
            case PUBLISHED:
            default:
                return ModerationStatus.ACCEPTED;
        }
    }

    @Override
    public long countPostsForModeration() {
        return entityRepository.countByActiveAndModerationStatus((byte)1, ModerationStatus.NEW);
    }

    @Override
    public Page<PostListItemDto> getModeratorPosts(
            int moderatorId,
            int offset,
            int limit,
            ModeratorPostListStatus status
    ) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.ASC, "time"));
        if (status == ModeratorPostListStatus.NEW) {
            return DtoMapper.toPostListDto(
                    viewRepository.findByActiveAndModerationStatus((byte) 1, ModerationStatus.NEW, pageable));
        }
        return DtoMapper.toPostListDto(
                viewRepository.findByActiveAndModeratorIdAndModerationStatus((byte) 1, moderatorId,
                        getModerationStatusForQuery(status), pageable));
    }

    private ModerationStatus getModerationStatusForQuery(ModeratorPostListStatus status) {
        switch (status) {
            case NEW:
                return ModerationStatus.NEW;
            case DECLINED:
                return ModerationStatus.DECLINED;
            case ACCEPTED:
            default:
                return ModerationStatus.ACCEPTED;
        }
    }

    @Transactional
    @Override
    public BaseResponseDto addPost(EditPostRequestDto requestDto) {
        Map<String, String> errors = new HashMap<>();
        validatePostData(requestDto, errors);
        BaseResponseDto responseDto = new BaseResponseDto();
        if (errors.isEmpty()) {
            User user = userRepository.getOne(requestDto.getUserId());
            ModerationStatus moderationStatus = user.isModerator() || !globalSettingService.isPostPremoderation() ?
                    ModerationStatus.ACCEPTED : ModerationStatus.NEW;

            Post post = new Post();
            post.setActive(requestDto.getActive());
            post.setModerationStatus(moderationStatus);
            post.setUser(user);
            post.setTime(validateTime(requestDto.getTimestamp()));
            post.setTitle(requestDto.getTitle());
            post.setText(requestDto.getText());
            post = entityRepository.save(post);

            createTags(requestDto, post);

            responseDto.setResult(true);
        } else {
            responseDto.setResult(false);
            responseDto.setErrors(errors);
        }
        return responseDto;
    }

    @Transactional
    @Override
    public BaseResponseDto editPost(int id, EditPostRequestDto requestDto) {
        Post post = entityRepository.getOne(id);
        User editor = userRepository.getOne(requestDto.getUserId());
        if (!editor.isModerator() && post.getUser().getId() != editor.getId()) {
            throw new AccessDeniedException(String.format("User %s cannot edit the post.", editor.getId()));
        }
        Map<String, String> errors = new HashMap<>();
        validatePostData(requestDto, errors);
        BaseResponseDto responseDto = new BaseResponseDto();
        if (errors.isEmpty()) {
            ModerationStatus moderationStatus = editor.isModerator() || !globalSettingService.isPostPremoderation() ?
                    ModerationStatus.ACCEPTED : ModerationStatus.NEW;

            post.setActive(requestDto.getActive());
            post.setModerationStatus(moderationStatus);
            post.setTime(validateTime(requestDto.getTimestamp()));
            post.setTitle(requestDto.getTitle());
            post.setText(requestDto.getText());
            recreateTags(requestDto, post);
            entityRepository.save(post);

            responseDto.setResult(true);
        } else {
            responseDto.setResult(false);
            responseDto.setErrors(errors);
        }
        return responseDto;
    }

    private void createTags(EditPostRequestDto requestDto, Post post) {
        if (requestDto.getTags() != null) {
            requestDto.getTags().forEach(tagName -> createPostTag(post, tagName));
        }
    }

    private PostTag createPostTag(Post post, String tagName) {
        PostTag postTag = new PostTag();
        postTag.setPost(post);
        postTag.setTag(createTag(tagName));
        return postTagRepository.save(postTag);
    }

    private Tag createTag(String tagName) {
        return tagRepository.findOneByNameIgnoreCase(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));
    }

    private void recreateTags(EditPostRequestDto requestDto, Post post) {
        if (requestDto.getTags() == null) {
            post.getTags().clear();
            return;
        }

        Map<String, PostTag> oldPostTags = post.getTags().stream().collect(Collectors.toMap(
                postTag -> postTag.getTag().getName().toUpperCase(Locale.ROOT),
                Function.identity()));

        Map<String, String> newTags = requestDto.getTags().stream().collect(Collectors.toMap(
                tagName -> tagName.toUpperCase(Locale.ROOT),
                Function.identity()));

        Set<String> tagsToCreate = new HashSet<>(newTags.keySet());
        tagsToCreate.removeAll(oldPostTags.keySet());

        oldPostTags.keySet().removeAll(newTags.keySet());
        post.getTags().removeAll(oldPostTags.values());

        newTags.keySet().retainAll(tagsToCreate);
        for (String tagName : newTags.values()) {
            post.getTags().add(createPostTag(post, tagName));
        }
    }

    private void validatePostData(EditPostRequestDto requestDto, Map<String, String> errors) {
        if (requestDto.getTitle() == null) {
            errors.put("title", "Заголовок не установлен");
        } else if (requestDto.getTitle().trim().length() < 3) {
            errors.put("title", "Заголовок слишком короткий");
        }
        if (requestDto.getText() == null) {
            errors.put("title", "Текст публикации не установлен");
        } else if (requestDto.getText().trim().length() < 50) {
            errors.put("title", "Текст публикации слишком короткий");
        }
    }

    private Date validateTime(long timestamp) {
        return new Date(Math.max(timestamp, System.currentTimeMillis()));
    }
}

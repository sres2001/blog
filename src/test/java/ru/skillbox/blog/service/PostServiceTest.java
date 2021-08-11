package ru.skillbox.blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.CalendarDto;
import ru.skillbox.blog.dto.PostDto;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.model.Post;
import ru.skillbox.blog.repository.PostRepository;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService service;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("recent возвращает отсортированные и самые новые посты")
    @Transactional
    public void testGetPosts() {
        Page<PostListItemDto> posts = service.getPosts(0, 10, PostListMode.RECENT);
        assertFalse(posts.isEmpty());

        Long timestamp = null;
        for (PostListItemDto post : posts) {
            if (timestamp == null || timestamp >= post.getTimestampAsEpochSeconds()) {
                timestamp = post.getTimestampAsEpochSeconds();
            } else {
                fail("Not ordered");
            }
        }

        Optional<Long> maxOptional = posts.stream()
                .map(PostListItemDto::getTimestampAsEpochSeconds).max(Long::compareTo);
        assertTrue(maxOptional.isPresent());
        long maxTimestamp = maxOptional.get();

        for (Post item : postRepository.findAllPublished()) {
            assertTrue(item.getTime().toInstant().getEpochSecond() <= maxTimestamp);
        }
    }

    @Test
    @DisplayName("находим посты текстовым поиском")
    @Transactional
    public void testSearchPosts() {
        String query = "слон";
        Page<PostListItemDto> posts = service.searchPosts(0, 10, query);
        assertFalse(posts.isEmpty());

        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        for (PostListItemDto item : posts) {
            Optional<Post> postOptional = postRepository.findById(item.getId());
            assertTrue(postOptional.isPresent());
            Post post = postOptional.get();
            assertTrue(pattern.matcher(post.getText()).find());
        }
    }

    @Test
    @DisplayName("получаем календарь постов")
    @Transactional
    public void testCalendar() {
        CalendarDto calendar = service.getPostsCalendarByYear(2021);
        assertNotNull(calendar.getYears());
        assertFalse(calendar.getYears().isEmpty());
        Integer prevYear = null;
        for (int i = 0; i < calendar.getYears().size(); i++) {
            Integer year = calendar.getYears().get(i);
            if (prevYear != null) {
                assertTrue(prevYear < year);
            }
            prevYear = year;

        }

        assertNotNull(calendar.getPosts());
        assertFalse(calendar.getPosts().isEmpty());

        LocalDate date = LocalDate.of(2021, Month.JUNE, 21);
        assertEquals(9, calendar.getPosts().get(date));
    }

    @Test
    @DisplayName("находим посты по дате")
    @Transactional
    public void testByDate() {
        LocalDate date = LocalDate.of(2021, Month.JUNE, 21);
        Page<PostListItemDto> posts = service.getPostsByDate(0, 10, date);
        assertFalse(posts.isEmpty());

        for (PostListItemDto item : posts) {
            Instant instant = Instant.ofEpochSecond(item.getTimestampAsEpochSeconds());
            ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
            assertEquals(date, dateTime.toLocalDate());
        }
    }

    @Test
    @DisplayName("находим посты по тэгу")
    @Transactional
    public void testByTag() {
        String tagName = "java";
        Page<PostListItemDto> posts = service.getPostsByTag(0, 10, tagName);
        assertFalse(posts.isEmpty());

        for (PostListItemDto item : posts) {
            Post post = postRepository.getOne(item.getId());
            assertTrue(post.getTags().stream()
                    .anyMatch(e -> e.getTag().getName().equalsIgnoreCase(tagName)));
        }
    }

    @Test
    @DisplayName("находим пост по идентификатору")
    @Transactional
    public void testFindPostById() {
        Optional<PostDto> postOptional = service.findPostById(46, null, false);
        assertTrue(postOptional.isPresent());
    }

    @Test
    @DisplayName("находим количество постов, ожидающих модерацию")
    @Transactional
    public void testCountPostsForModeration() {
        assertEquals(1, service.countPostsForModeration());
    }

}

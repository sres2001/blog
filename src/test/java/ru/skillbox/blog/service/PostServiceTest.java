package ru.skillbox.blog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.blog.api.request.PostListMode;
import ru.skillbox.blog.dto.PostListItemDto;
import ru.skillbox.blog.model.Post;
import ru.skillbox.blog.repository.PostRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

        for (Post item : postRepository.getAllPublished()) {
            assertTrue(item.getTime().toInstant().getEpochSecond() <= maxTimestamp);
        }
    }
}

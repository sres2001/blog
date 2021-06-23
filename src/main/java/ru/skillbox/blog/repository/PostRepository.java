package ru.skillbox.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.ModerationStatus;
import ru.skillbox.blog.model.Post;

import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findByActiveAndModerationStatusAndTimeLessThanEqual(
            byte active,
            ModerationStatus moderationStatus,
            Date timeThreshold,
            Pageable pageable);

    default Page<Post> getAllPublished() {
        return findByActiveAndModerationStatusAndTimeLessThanEqual((byte) 1,
                ModerationStatus.ACCEPTED,
                new Date(),
                Pageable.unpaged());
    }

    long countByActiveAndModerationStatusAndTimeLessThanEqual(
            byte active,
            ModerationStatus moderationStatus,
            Date timeThreshold);

    default long countAllPublished() {
        return countByActiveAndModerationStatusAndTimeLessThanEqual((byte) 1,
                ModerationStatus.ACCEPTED,
                new Date());
    }
}

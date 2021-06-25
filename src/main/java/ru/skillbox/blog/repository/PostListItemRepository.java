package ru.skillbox.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.ModerationStatus;
import ru.skillbox.blog.model.PostListItem;

import java.util.Date;

@Repository
public interface PostListItemRepository extends PagingAndSortingRepository<PostListItem, Integer> {

    Page<PostListItem> findByActiveAndModerationStatusAndTimeLessThanEqual(
            byte active,
            ModerationStatus moderationStatus,
            Date timeThreshold,
            Pageable pageable);

    default Page<PostListItem> findPublished(Pageable pageable) {
        return findByActiveAndModerationStatusAndTimeLessThanEqual(
                (byte)1,
                ModerationStatus.ACCEPTED,
                new Date(),
                pageable);
    }

    Page<PostListItem> findByActiveAndModerationStatusAndTimeLessThanEqualAndTextContainingIgnoreCase(
            byte active,
            ModerationStatus moderationStatus,
            Date timeThreshold,
            String query,
            Pageable pageable);

    default Page<PostListItem> searchPublished(String query, Pageable pageable) {
        return findByActiveAndModerationStatusAndTimeLessThanEqualAndTextContainingIgnoreCase(
                (byte)1,
                ModerationStatus.ACCEPTED,
                new Date(),
                query,
                pageable);
    }
}

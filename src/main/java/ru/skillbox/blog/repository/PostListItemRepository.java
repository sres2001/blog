package ru.skillbox.blog.repository;

import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.ModerationStatus;
import ru.skillbox.blog.model.PostListItem;
import ru.skillbox.blog.model.Tag;

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

    @Query("select p from PostListItem p" +
            " where active = 1 and moderationStatus = 'ACCEPTED' and time <= current_timestamp()" +
            "   and cast(time as date) = cast(:date as date)")
    Page<PostListItem> findPublishedByDate(Date date, Pageable pageable);

    @Query("select p from PostListItem p" +
            " join PostTag pt on pt.post = p" +
            " where p.active = 1 and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp()" +
            "   and pt.tag = :tag")
    Page<PostListItem> findPublishedByTag(Tag tag, Pageable pageable);

    Page<PostListItem> findByUserIdAndActive(
            int userId,
            byte active,
            Pageable pageable);

    Page<PostListItem> findByUserIdAndActiveAndModerationStatus(
            int userId,
            byte active,
            ModerationStatus moderationStatus,
            Pageable pageable);

    Page<PostListItem> findByActiveAndModerationStatus(
            byte active,
            ModerationStatus moderationStatus,
            Pageable pageable);

    Page<PostListItem> findByActiveAndModeratorIdAndModerationStatus(
            byte active,
            int moderatorId,
            ModerationStatus moderationStatus,
            Pageable pageable);
}

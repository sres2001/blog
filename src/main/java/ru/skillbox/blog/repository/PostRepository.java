package ru.skillbox.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.ModerationStatus;
import ru.skillbox.blog.model.Post;

import java.util.Date;
import java.util.List;

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

    @Query("select distinct year(time) from Post" +
            " where active = 1 and moderationStatus = 'ACCEPTED' and time <= current_timestamp()")
    List<Integer> getPublishedPostsYears();

    @Query("select new ru.skillbox.blog.repository.DateAndCount(year(time), month(time), day(time), count(*)) from Post" +
            " where active = 1 and moderationStatus = 'ACCEPTED' and time <= current_timestamp()" +
            "   and year(time) = :year" +
            " group by year(time), month(time), day(time)")
    List<DateAndCount> getPublishedPostsCountsByDatesInYear(int year);
}

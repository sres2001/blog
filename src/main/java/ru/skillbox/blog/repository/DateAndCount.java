package ru.skillbox.blog.repository;

import java.time.LocalDate;

public class DateAndCount {
    private final LocalDate date;
    private final long postsCount;

    public DateAndCount(int year, int month, int day, long postsCount) {
        this.date = LocalDate.of(year, month, day);
        this.postsCount = postsCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getPostsCount() {
        return postsCount;
    }
}

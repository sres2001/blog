package ru.skillbox.blog.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class DateAndCount {
    private final LocalDate date;
    private final long postsCount;

    public DateAndCount(Date date, long postsCount) {
        this.date = LocalDate.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC);
        this.postsCount = postsCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getPostsCount() {
        return postsCount;
    }
}

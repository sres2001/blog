package ru.skillbox.blog.repository;

import java.time.Instant;

public interface PostsStatistics {
    long getPostsCount();
    long getLikesCount();
    long getDislikesCount();
    long getViewsCount();
    Instant getFirstPublication();
}

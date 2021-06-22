package ru.skillbox.blog.repository;

public class TagAndCount {
    private final String name;
    private final long postsCount;

    public TagAndCount(String name, long postsCount) {
        this.name = name;
        this.postsCount = postsCount;
    }

    public String getName() {
        return name;
    }

    public long getPostsCount() {
        return postsCount;
    }
}

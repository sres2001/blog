package ru.skillbox.blog.api.response;

import java.util.List;

public class PostListResponse {

    private long count;
    private List<PostListItemResponse> posts;

    public PostListResponse(long count, List<PostListItemResponse> posts) {
        this.count = count;
        this.posts = posts;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<PostListItemResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostListItemResponse> posts) {
        this.posts = posts;
    }
}

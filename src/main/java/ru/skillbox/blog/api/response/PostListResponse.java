package ru.skillbox.blog.api.response;

import java.util.List;

public class PostListResponse {

    private int count;
    private List<PostResponse> posts;

    public PostListResponse(int count, List<PostResponse> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}

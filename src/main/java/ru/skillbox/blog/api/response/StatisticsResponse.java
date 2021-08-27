package ru.skillbox.blog.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticsResponse {

    private long postsCount;
    private long likesCount;
    private long dislikesCount;
    private long viewsCount;
    @JsonProperty("firstPublication")
    private Long firstPublicationAsEpochSeconds;

    public long getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(long postsCount) {
        this.postsCount = postsCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(long dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Long getFirstPublicationAsEpochSeconds() {
        return firstPublicationAsEpochSeconds;
    }

    public void setFirstPublicationAsEpochSeconds(Long firstPublicationAsEpochSeconds) {
        this.firstPublicationAsEpochSeconds = firstPublicationAsEpochSeconds;
    }
}

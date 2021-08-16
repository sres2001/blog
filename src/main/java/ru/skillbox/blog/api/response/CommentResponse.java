package ru.skillbox.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentResponse {

    private int id;
    @JsonProperty("parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer parentId;
    @JsonProperty("timestamp")
    private long timestampAsEpochSeconds;
    private String text;
    private CommentAuthorResponse user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public long getTimestampAsEpochSeconds() {
        return timestampAsEpochSeconds;
    }

    public void setTimestampAsEpochSeconds(long timestampAsEpochSeconds) {
        this.timestampAsEpochSeconds = timestampAsEpochSeconds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CommentAuthorResponse getUser() {
        return user;
    }

    public void setUser(CommentAuthorResponse user) {
        this.user = user;
    }
}

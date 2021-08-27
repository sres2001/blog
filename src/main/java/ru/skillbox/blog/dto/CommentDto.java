package ru.skillbox.blog.dto;

public class CommentDto {
    private int id;
    private Integer parentId;
    private long timestampAsEpochSeconds;
    private String text;
    private CommentAuthorDto user;

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

    public CommentAuthorDto getUser() {
        return user;
    }

    public void setUser(CommentAuthorDto user) {
        this.user = user;
    }
}

package ru.skillbox.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationRequest {

    @JsonProperty("post_id")
    private int postId;
    private ModerationDecision decision;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public ModerationDecision getDecision() {
        return decision;
    }

    public void setDecision(ModerationDecision decision) {
        this.decision = decision;
    }
}

package ru.skillbox.blog.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import ru.skillbox.blog.model.ModerationStatus;

import java.util.Locale;

public enum ModerationDecision {
    ACCEPT(ModerationStatus.ACCEPTED),
    DECLINE(ModerationStatus.DECLINED);

    private final ModerationStatus moderationStatus;

    ModerationDecision(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    @JsonCreator
    public static ModerationDecision fromString(String s) {
        return valueOf(s.toUpperCase(Locale.ROOT));
    }
}

package ru.skillbox.blog.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.util.Date;

@Entity
@Immutable
@Subselect("select psub.*,\n" +
        "coalesce(likes, 0) as likes, coalesce(dislikes, 0) as dislikes, coalesce(comments, 0) as comments from\n" +
        "(select *\n" +
        "   from posts\n" +
        ")\n" +
        "psub left join\n" +
        "(select post_id,\n" +
        "        sum(case when value = 1 then 1 else 0 end) likes,\n" +
        "        sum(case when value = -1 then 1 else 0 end) dislikes\n" +
        "   from post_votes\n" +
        "  group by post_id\n" +
        ")\n" +
        "vsub on psub.id = vsub.post_id left join\n" +
        "(select post_id,\n" +
        "        sum(1) comments\n" +
        "   from post_comments\n" +
        "  group by post_id\n" +
        ")\n" +
        "csub on psub.id = csub.post_id")
public class PostListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte active;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", nullable = false,
            columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
    private ModerationStatus moderationStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "likes", nullable = false)
    private int likeCount;

    @Column(name = "dislikes", nullable = false)
    private int dislikeCount;

    @Column(name = "comments", nullable = false)
    private int commentCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}

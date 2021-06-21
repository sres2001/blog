package ru.skillbox.blog.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.util.Date;

@Entity
@Immutable
@Subselect("select id, time, user_id, title, text, view_count,\n" +
        "coalesce(likes, 0) as likes, coalesce(dislikes, 0) as dislikes, coalesce(comments, 0) as comments from\n" +
        "(select p.*\n" +
        "  from posts p\n" +
        " where p.is_active = 1\n" +
        "   and p.moderation_status = 'ACCEPTED'\n" +
        "   and p.time <= now()\n" +
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
public class PostView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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

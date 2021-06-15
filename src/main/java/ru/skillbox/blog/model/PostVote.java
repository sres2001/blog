package ru.skillbox.blog.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "post_votes")
public class PostVote {
    private int id;
    private User user;
    private Post post;
    private Date time;
    private byte value;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    @Column(name = "time", nullable = false)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Column(name = "value", nullable = false)
    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}

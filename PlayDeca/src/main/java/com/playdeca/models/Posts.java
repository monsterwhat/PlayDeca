package com.playdeca.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Posts")
public class Posts extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    
    @ManyToOne
    @JoinColumn(name = "threadId")
    private Threads thread;
    
    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;
    
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;
    
    private String title;
    
    private String body;
    
    private int upvotes;
    
    private int downvotes;

    public Posts() {
    }

    public Posts(Long postId, Threads thread, Users user, LocalDateTime date, String title, String body, int upvotes, int downvotes) {
        this.postId = postId;
        this.thread = thread;
        this.user = user;
        this.date = date;
        this.title = title;
        this.body = body;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Threads getThread() {
        return thread;
    }

    public void setThread(Threads thread) {
        this.thread = thread;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
}
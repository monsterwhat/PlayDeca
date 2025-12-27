package com.playdeca.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Threads")
public class Threads extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long threadId;

    @ManyToOne
    @JoinColumn(name = "userID")
    private Users user;

    private String title;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    public Threads() {
    }

    public Threads(Long threadId, Users user, String title, LocalDateTime date) {
        this.threadId = threadId;
        this.user = user;
        this.title = title;
        this.date = date;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Threads{");
        sb.append("threadId=").append(threadId);
        sb.append(", user=").append(user);
        sb.append(", title=").append(title);
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}
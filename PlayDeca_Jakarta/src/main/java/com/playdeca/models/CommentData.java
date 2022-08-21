package com.playdeca.models;

import java.util.Date;

public class CommentData {
    
    private String content;
    private UserData author;
    private Date date_published;

    //Jakarta 9 JSON Files requiere NO CONTRUCTORS

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserData getAuthor() {
        return author;
    }

    public void setAuthor(UserData author) {
        this.author = author;
    }

    public Date getDate_published() {
        return date_published;
    }

    public void setDate_published(Date date_published) {
        this.date_published = date_published;
    }
    
    
}

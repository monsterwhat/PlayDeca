package com.playdeca.models;

import java.util.Date;

public class NewsData {
    
    private int ID;
    private String title;
    private String content;
    private String author;
    private Date date;

        //Jakarta 9 JSON Files requiere NO CONTRUCTORS

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
}

package com.playdeca.models;

import java.util.Date;

public class ForumData {
    
    private String title;
    private String content;
    private UserData author;
    private Date date_published;
    private int vote_count;
    private VoteData votes;
    private CommentData comment;

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

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public VoteData getVotes() {
        return votes;
    }

    public void setVotes(VoteData votes) {
        this.votes = votes;
    }

    public CommentData getComment() {
        return comment;
    }

    public void setComment(CommentData comment) {
        this.comment = comment;
    }

    
}

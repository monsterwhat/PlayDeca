package Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "Posts")
public class Posts implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    
    @ManyToOne
    @JoinColumn(name = "threadId")
    private Threads thread;
    
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    private String title;
    
    private String body;
    
    private int upvotes;
    
    private int downvotes;

    public Posts() {
    }

    public Posts(int postId, Threads thread, User user, Date date, String title, String body, int upvotes, int downvotes) {
        this.postId = postId;
        this.thread = thread;
        this.user = user;
        this.date = date;
        this.title = title;
        this.body = body;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
    
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Threads getThread() {
        return thread;
    }

    public void setThread(Threads thread) {
        this.thread = thread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

package Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

/**
 *
 * @author Al
 */
@Entity
@Table(name = "Reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyID;
    
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "PostID")
    private Post post;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreationDate")
    private Date creationDate;
    
    private String content;

    public Reply() {
    }

    public Reply(Long replyID, User user, Post post, Date creationDate, String content) {
        this.replyID = replyID;
        this.user = user;
        this.post = post;
        this.creationDate = creationDate;
        this.content = content;
    }

    public Long getReplyID() {
        return replyID;
    }

    public void setReplyID(Long replyID) {
        this.replyID = replyID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    
}
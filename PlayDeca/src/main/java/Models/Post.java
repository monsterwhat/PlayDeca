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
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Al
 */

@Entity
@Table(name = "Post")
public class Post implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;
    
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Category category;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreationDate")
    private Date creationDate;
    
    private String title;
    
    private String content;

    public Post() {
    }

    public Post(Long postID, User user, Category category, Date creationDate, String title, String content) {
        this.postID = postID;
        this.user = user;
        this.category = category;
        this.creationDate = creationDate;
        this.title = title;
        this.content = content;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
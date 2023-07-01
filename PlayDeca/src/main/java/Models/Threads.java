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
import java.util.Date;

@Entity
@Table(name = "Threads")
public class Threads implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int threadId;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Threads() {
    }

    public Threads(int threadId, User user, String title, Date date) {
        this.threadId = threadId;
        this.user = user;
        this.title = title;
        this.date = date;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
}

package Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */

@Entity
@Table(name = "Users")
public class Users implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    
    private String username;
    private String password;
    private String UUID;
    private String email;
    private String userGroup;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ServerLogs> ServerLogs;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RegistrationDate")
    private Date registrationDate;

    public Users() {
    }

    public Users(Long userID, String username, String password, String UUID, String email, String userGroup, Date registrationDate) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.UUID = UUID;
        this.email = email;
        this.userGroup = userGroup;
        this.registrationDate = registrationDate;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }
    
    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Users{" + "userID=" + userID + ", username=" + username + ", password=" + password + ", UUID=" + UUID + ", email=" + email + ", userGroup=" + userGroup + ", registrationDate=" + registrationDate + '}';
    }


}

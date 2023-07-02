package Controllers;

import Models.Users;
import Services.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Al
 */

@Named(value = "SessionController")
@SessionScoped
public class SessionController implements Serializable{

    public SessionController() {
    }
    
    private String username;
    private String password;
    
    private Users currentUser;
    
    @Inject
    private UserService UserService;
        
    public String login(){
        boolean isValid = UserService.login(username, password);
        if(isValid){
            currentUser = UserService.getSession(username, password);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Login successful!"));
            return "index.xhtml?faces-redirect=true";
        }else{
            return null;
        }
    }
    
    public boolean isValid(){
        return currentUser != null;
    }
    
    public boolean isAdmin(){
        return "administrator".equals(currentUser.getRole());
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

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public UserService getUserService() {
        return UserService;
    }

    public void setUserService(UserService UserService) {
        this.UserService = UserService;
    }

}
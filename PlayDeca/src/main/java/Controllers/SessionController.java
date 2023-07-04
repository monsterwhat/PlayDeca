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
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String newEmail;
    private String AuthCode;
    
    private boolean hasPassword;
    
    private Users currentUser;
    
    @Inject
    private UserService UserService;
        
    public String login(){
        boolean isValid = UserService.login(username, password);
        if(isValid){
            currentUser = UserService.getSession(username, password);
            this.hasPassword = true;
            this.username = null;
            this.password = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Login successful!"));
            return "index.xhtml?faces-redirect=true";
        }else{
            return null;
        }
    }
    
    public String logOut(){
        this.currentUser = null;
        this.hasPassword=false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
        return "index.xhtml?faces-redirect=true";
    }
    
    public boolean isValid(){
        return currentUser != null;
    }
    
    public void updateEmail(){
        
    }
    
    public boolean isAdmin(){
        if(isValid() && "Administrator".equals(currentUser.getRole())){
            return true;
        }else{
            return false;
        }
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getAuthCode() {
        return AuthCode;
    }

    public void setAuthCode(String AuthCode) {
        this.AuthCode = AuthCode;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }
    
    

}

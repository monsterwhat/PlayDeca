package Controllers;

import Models.Users;
import Services.LogsService;
import Services.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.Principal;

/**
 *
 * @author Al
 */

@BasicAuthenticationMechanismDefinition(
  realmName = "jdbcRealm")
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
    SecurityContext securityContext;
    
    @Inject private LogsService logger;
    @Inject private UserService UserService;
            
    public String login(){

        try {
            Principal principal = securityContext.getCallerPrincipal();
            if (null == principal) {
            // User not authenticated, attempt to log in using provided username and password.
            currentUser = UserService.getSession(username, password);

            if (currentUser != null) {
                this.hasPassword = true;
                this.username = null;
                this.password = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Login successful!"));
                logger.createLog("User: " + currentUser.getUsername() + " logged In", "Successful user Login", currentUser);
                return "index.xhtml?faces-redirect=true";
            } else {
                // Authentication failed
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password."));
                logger.createLog("Failed login attempt", "Invalid username or password", null);
            }
        }
        } catch (Exception e) {
            System.out.println("Error: "+ e.getLocalizedMessage());
        }
        return null;
    }
    
    public String logOut(){
        logger.createLog("User: "+currentUser.getUsername()+" logged out","User Logged Out",currentUser);
        this.currentUser = null;
        this.hasPassword=false;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
        return "index.xhtml?faces-redirect=true";
    }
    
    public boolean isValid(){
        return currentUser != null;
    }
    
    public void updateEmail() {
        if (confirmPassword.equals(currentUser.getPassword())) {
            if (!newEmail.equals(currentUser.getEmail())) {
                currentUser.setEmail(newEmail);
                UserService.updateUser(currentUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Email updated successfully."));
                logger.createLog("Updated Email", "", currentUser);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "New email is same as current email."));
                logger.createLog("Failed to update Email", "Used the same email", currentUser);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect password."));
            logger.createLog("Failed to update Email", "User inputted incorrect password", currentUser);
        }
    }
    
    public void updatePassword(){
        if(oldPassword.equals(currentUser.getPassword())){
            if(AuthCode.equals(AuthCode)){
                if(newPassword.equals(confirmPassword)){
                    currentUser.setPassword(newPassword);
                    
                    //UserService.updateUser(currentUser);
                    logger.createLog("Updated password", "User: "+ currentUser.getUsername() +" updated password", currentUser);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password updated successfully."));
                }else{
                logger.createLog("Tried to update password", "Incorrect password", currentUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Passwords do not match."));
                }
            }else{
            logger.createLog("Tried to update password", "Incorrect authCode", currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect Authentication Code."));
            }
        }else{
        logger.createLog("Tried to update password", "Incorrect Auth Password", currentUser);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect password."));
        }
    }
    
    public boolean isAdmin(){
        if(isValid() && "Admin".equals(currentUser.getRank().getGroupName())){
            return true;
        }else{
            return false;
        }
    }

    public void navigateToAdminDashboard() {
    if (isAdmin()) {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler navHandler = context.getApplication().getNavigationHandler();
        navHandler.handleNavigation(context, null, "admin-dashboard.xhtml?faces-redirect=true");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Access denied. You need admin privileges."));
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

    public LogsService getLogger() {
        return logger;
    }

    public void setLogger(LogsService logger) {
        this.logger = logger;
    }

    
    
}

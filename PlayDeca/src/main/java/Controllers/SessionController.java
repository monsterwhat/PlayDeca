package Controllers;

import Models.Users;
import Services.LogsService;
import Services.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static jakarta.security.enterprise.AuthenticationStatus.SUCCESS;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
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
    
    @NotEmpty private String username;
    @NotEmpty private String password;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String newEmail;
    private String AuthCode;
    private String UUID;
    private Users currentUser;
    
    @Inject private LogsService logger;
    @Inject private UserService UserService;
    
    @Inject FacesContext facesContext;
    @Inject SecurityContext securityContext;

    public void executeLogin(){
        try {
            switch (processAuthentication()) {
                case SEND_CONTINUE -> facesContext.responseComplete();
                case SEND_FAILURE -> {
                    logger.createLog("Failed login attempt", "Invalid username or password", null);
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password."));
                }
                case SUCCESS -> {
                    currentUser = UserService.getSession(securityContext.getCallerPrincipal().getName());
                    logger.createLog("User: " + currentUser.getUsername() + " logged In", "Successful user Login", currentUser);
                    getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/");
                }
                case NOT_DONE -> {
                }
                default -> throw new AssertionError();
            }
            
        } catch (IOException e) {
            System.out.println("Error logging in " + e.getLocalizedMessage());
        }
    }
    
    public String logOut(){
        try {
            logger.createLog("User: "+currentUser.getUsername()+" logged out","User Logged Out",currentUser);
            this.currentUser = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
            ExternalContext ec = facesContext.getExternalContext();
            ((HttpServletRequest)ec.getRequest()).logout();
            return "/index.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            
        }
        return null;
    }
    
    public boolean isValid(){
        return currentUser != null;
    }
    
    public void updateUUID(){
        if(UUID != null){
            currentUser.setUUID(UUID);
            UserService.update(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "UUID updated successfully."));
            logger.createLog("Updated UUID", "", currentUser);
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Warning", "UUID cannot be null."));
            logger.createLog("Tried to update UUID", "Null UUID given", currentUser);
        }
    }
    
    public void updateEmail() {
        var verified = UserService.verifyPassword(confirmPassword.toCharArray(), currentUser.getPassword());
        if (verified) {
            if (!newEmail.equals(currentUser.getEmail())) {
                currentUser.setEmail(newEmail);
                UserService.update(currentUser);
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
        var verified = UserService.verifyPassword(confirmPassword.toCharArray(), currentUser.getPassword());
        if(verified){
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
        if(isValid() && "admin".equals(currentUser.getUserGroup())){
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

    private AuthenticationStatus processAuthentication(){
        ExternalContext ec = getExternalContext();
        return securityContext.authenticate(
                (HttpServletRequest)ec.getRequest(),
                (HttpServletResponse)ec.getResponse(),
                AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(username,password)));
    }
    
    private ExternalContext getExternalContext(){
     return facesContext.getExternalContext();
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

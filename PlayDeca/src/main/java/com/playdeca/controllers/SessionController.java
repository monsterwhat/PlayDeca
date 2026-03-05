package com.playdeca.controllers;

import com.playdeca.models.Users;
import com.playdeca.services.LogsService;
import com.playdeca.services.MailerService;
import com.playdeca.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Al
 */
@Named(value = "SessionController")
@SessionScoped
@Data
public class SessionController implements Serializable {

    public SessionController() {
    }
     
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String newEmail;
    private String AuthCode;
    private String UUID;

    private Users currentUser;

    private String selectedOption = "login";

    @NotEmpty
    private String newUserUsername;
    @NotEmpty
    private String newUserEmail;
    @NotEmpty
    private String newUserPassword;
    @NotEmpty
    private String newUserVerifyPassword;

    @Inject
    private LogsService logsService;
    @Inject
    private UserService userService;

    @Inject
    FacesContext facesContext;
    
    @Inject
    private MailerService mailer; 
 
    public void executeLogin() {
        try {
            if (userService.login(username, password)) {
                currentUser = userService.getSession(username);
                logsService.createLog("User: " + currentUser.getUsername() + " logged In", "Successful user Login", currentUser);
                getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/");
            } else {
                logsService.createLog("Failed login attempt", "Invalid username or password", null);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password."));
            }
        } catch (IOException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Login failed: " + e.getMessage()));
        }
    }

    public void logout() {
        try {
            // Create simple log without accessing user object to avoid lazy loading
            if (currentUser != null) {
                // Use a simple string without calling getUsername() to avoid lazy initialization
                logsService.createLog("User logout", "Successful user Logout", null);
            }
           
            HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
            request.logout();
           
            // Invalidate session
            currentUser = null;
            getExternalContext().invalidateSession();
           
            // Redirect to login page
            getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/login.xhtml");
        } catch (ServletException | IOException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Logout failed: " + e.getMessage()));
        }
    }

    public void register() {
        try {
            if (!newUserPassword.equals(newUserVerifyPassword)) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match."));
                return;
            }

            if (userService.doesUsernameAlreadyExists(newUserUsername)) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username already exists."));
                return;
            }

            if (userService.doesEmailAlreadyExists(newUserEmail)) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already exists."));
                return;
            }

            Users newUser = new Users();
            newUser.setUsername(newUserUsername);
            newUser.setEmail(newUserEmail);
            newUser.setPassword(newUserPassword);
            newUser.setUserGroup("user");
            newUser.setUUID(java.util.UUID.randomUUID().toString());

            userService.register(newUser);

            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Registration successful. You can now login."));
            
            // Clear form
            newUserUsername = "";
            newUserEmail = "";
            newUserPassword = "";
            newUserVerifyPassword = "";
            
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Registration failed: " + e.getMessage()));
        }
    }

    public boolean isValid() {
        return currentUser != null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getUserGroup());
    }

    public boolean isUser() {
        return currentUser != null && "user".equals(currentUser.getUserGroup());
    }

    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public void requireLogin() throws IOException {
        if (!isLoggedIn()) {
            getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/login.xhtml");
        }
    }
    
    public void navigateToHome() throws IOException {
        getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/");
    }
    
    public void navigateToAdminDashboard() throws IOException {
        if (isAdmin()) {
            getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/secured/admin-dashboard.xhtml");
        } else {
            getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/");
        }
    }

    public void loglessOut() {
        try {
            currentUser = null;
            getExternalContext().invalidateSession();
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Logout failed: " + e.getMessage()));
        }
    }

    public LogsService getLogger() {
        return logsService;
    }

    public void setLogger(LogsService logger) {
        this.logsService = logger;
    }
    
    public void selectRegister() {
        selectedOption = "register";
    }
    
    public void selectLogin() {
        selectedOption = "login";
    }
    
    public void registerUser() {
        register();
    }
    
    public void updatePassword() {
        try {
            if (oldPassword == null || oldPassword.isEmpty()) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please enter your current password."));
                return;
            }
            
            if (newPassword == null || newPassword.isEmpty()) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please enter a new password."));
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "New passwords do not match."));
                return;
            }
            
            if (currentUser == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to change your password."));
                return;
            }
            
            userService.updatePassword(currentUser, oldPassword, newPassword);
            
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password updated successfully."));
            
            oldPassword = "";
            newPassword = "";
            confirmPassword = "";
            
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update password: " + e.getMessage()));
        }
    }
}
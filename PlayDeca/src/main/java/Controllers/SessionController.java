package Controllers;

import Models.Users;
import Services.LogsService;
import Services.MailerService;
import Services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import static jakarta.security.enterprise.AuthenticationStatus.NOT_DONE;
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
    private LogsService logger;
    @Inject
    private UserService UserService;

    @Inject
    FacesContext facesContext;
    
    @Inject
    SecurityContext securityContext;

    @Inject
    private MailerService mailer; 
 
    public void executeLogin() {
        try {
            switch (processAuthentication()) {
                case SEND_CONTINUE ->
                    facesContext.responseComplete();
                case SEND_FAILURE -> {
                    logger.createLog("Failed login attempt", "Invalid username or password", null);
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password."));
                }
                case SUCCESS -> {
                    if (securityContext.getCallerPrincipal().getName() != null) {
                        currentUser = UserService.getSession(securityContext.getCallerPrincipal().getName());
                        logger.createLog("User: " + currentUser.getUsername() + " logged In", "Successful user Login", currentUser);
                        getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/");

                    }
                }
                case NOT_DONE -> {
                }

                default ->
                    throw new AssertionError();
            }

        } catch (IOException e) {
            System.out.println("Error logging in " + e.getLocalizedMessage());
        }
    }

    public String logOut() {
        try {
            logger.createLog("User: " + currentUser.getUsername() + " logged out", "User Logged Out", currentUser);

            this.currentUser = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
            ExternalContext ec = facesContext.getExternalContext();
            ((HttpServletRequest) ec.getRequest()).logout();
            return "/index?faces-redirect=true";
        } catch (ServletException e) {

        }
        return null;
    }

    public String loglessOut() {
        try {
            this.currentUser = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
            ExternalContext ec = facesContext.getExternalContext();
            ((HttpServletRequest) ec.getRequest()).logout();
            return "/index?faces-redirect=true";
        } catch (ServletException e) {

        }
        return null;
    }

    public boolean isValid() {
        return currentUser != null;
    }

    public void updateUUID() {
        if (UUID != null) {
            currentUser.setUUID(UUID);
            UserService.update(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "UUID updated successfully."));
            logger.createLog("Updated UUID", "", currentUser);
        } else {
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

    public void updatePassword() {
        var verified = UserService.verifyPassword(confirmPassword.toCharArray(), currentUser.getPassword());
        if (verified) {
            if (AuthCode.equals(AuthCode)) {
                if (newPassword.equals(confirmPassword)) {
                    currentUser.setPassword(newPassword);

                    //UserService.updateUser(currentUser);
                    logger.createLog("Updated password", "User: " + currentUser.getUsername() + " updated password", currentUser);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Password updated successfully."));
                } else {
                    logger.createLog("Tried to update password", "Incorrect password", currentUser);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Passwords do not match."));
                }
            } else {
                logger.createLog("Tried to update password", "Incorrect authCode", currentUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect Authentication Code."));
            }
        } else {
            logger.createLog("Tried to update password", "Incorrect Auth Password", currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect password."));
        }
    }

    public void selectLogin() {
        selectedOption = "login";
    }

    public void selectRegister() {
        selectedOption = "register";
    }

    public void registerUser() {
        try {
            if (verifyRegisteredUserData()) {
                Users newUser = new Users();
                newUser.setUsername(newUserUsername);
                newUser.setEmail(newUserEmail);
                newUser.setPassword(newUserPassword);
                newUser.setUserGroup("user");

                UserService.register(newUser);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully registered!"));
                mailer.sendEmail(newUserEmail, "Successfully registered", "Welcome to Playdeca! Please enjoy your stay");
                getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/");
            }
        } catch (Exception e) {
            System.out.println("Error registering user!");
        }
    }

    public boolean verifyRegisteredUserData() {
        String password = newUserPassword;
        String passwordV = newUserVerifyPassword;
        String username = newUserUsername;
        String email = newUserEmail;

        List<String> warnMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        // Check if the email is already registered
        if (UserService.doesEmailAlreadyExists(email)) {
            errorMessages.add("Email is already registered.");
        }

        // Check if the username already exists
        if (UserService.doesUsernameAlreadyExists(username)) {
            errorMessages.add("Username already exists.");
        }

        // Check if any of the required fields is null
        if (password == null || passwordV == null || username == null || email == null) {
            warnMessages.add("Please fill out all of the fields.");
        }

        // Check if the passwords match
        if (!password.equals(passwordV)) {
            warnMessages.add("Passwords do not match.");
        }

        // Check if the password meets the criteria
        if (!password.matches(".*[A-Z].*")
                || // At least one capital letter
                !password.matches(".*[0-9].*")
                || // At least one digit
                !password.matches(".*[!@#$%^&*()].*")) { // At least one special character
            warnMessages.add("Password format is incorrect. Please verify that your password contains at least one capital letter, digit, and a special character:'!@#$%^&*()'.");
        }

        // Check if the email follows the *@*.* format using a regular expression
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            warnMessages.add("Invalid email format. Please enter a valid email address.");
        }

        // Add all messages to the FacesContext
        FacesContext facesContext = FacesContext.getCurrentInstance();
        for (String errorMessage : errorMessages) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", errorMessage));
        }

        // If there are any other error messages, add them to the FacesContext with SEVERITY_WARN
        if (!warnMessages.isEmpty() || !errorMessages.isEmpty()) {
            for (String errorMessage : warnMessages) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validation Error", errorMessage));
            }
            return false;
        }

        return true;
    }

    public boolean isAdmin() {
        if (isValid() && "admin".equals(currentUser.getUserGroup())) {
            return true;
        } else {
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

    private AuthenticationStatus processAuthentication() {
        try {
            ExternalContext ec = getExternalContext();
            return securityContext.authenticate(
                    (HttpServletRequest) ec.getRequest(),
                    (HttpServletResponse) ec.getResponse(),
                    AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(username, password)));
        } catch (Exception e) {
            System.out.println("Authentication Error");
            return null;
        }
    }

    private ExternalContext getExternalContext() {
        return facesContext.getExternalContext();
    }

}

package Controllers;

import Services.Service;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Al
 */

@Named(value = "Controller")
@RequestScoped
public class Controller implements Serializable{

    public Controller() {
    }
    
    @Inject
    private Service service;
    
    private String username;
    private String password;
        
    public String loginDB() {
        boolean estado = service.login(username, password);
        if (estado) {
            return "index.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", null));
            return null;
        }
    }
    
    public String login() {
        // Authenticate the user using the provided username and password
        if (username.equals("admin") && password.equals("password")) {
                return "index.xhtml?faces-redirect=true";
        } else {
            // Invalid credentials, show error message
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid credentials"));
            return null;
        }
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

    
    
}

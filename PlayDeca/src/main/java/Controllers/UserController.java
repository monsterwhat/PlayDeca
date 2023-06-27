package Controllers;

import Services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Al
 */

@Named(value="UserController")
@RequestScoped
public class UserController implements Serializable{

    public UserController() {
    }
    
    @Inject
    private UserService UserService;

    public UserService getUserService() {
        return UserService;
    }

    public void setUserService(UserService UserService) {
        this.UserService = UserService;
    }
}

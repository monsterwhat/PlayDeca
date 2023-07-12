package Controllers;

import Models.Users;
import Services.LogsService;
import Services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Al
 */

@Named(value="UserController")
@ViewScoped
public class UserController implements Serializable{

    @Inject private UserService UserService;

    private List<Users> users;
    private Users selectedUser;
    private Users newUser;
    
    public UserController() {
        
    }
    
    @PostConstruct
    public void init(){
        newUser = new Users();
        selectedUser = new Users();
    }
    
    public List<Users> getUsers() {
        if (users == null) {
            users = UserService.listAll();
        }
        return users;
    }
    
    public void openNewUser() {
        newUser = new Users();
    }
    
    public UserService getUserService() {
        return UserService;
    }

    public void setUserService(UserService UserService) {
        this.UserService = UserService;
    }
    
    public void saveUser() {
        UserService.updateUser(selectedUser);
        clearSelectedUser();
    }
    
    public void createUser(){
        UserService.createUser(newUser);
        clearSelectedUser();
    }

    public void deleteUser() {
        if(selectedUser !=null){
            UserService.deleteUser(selectedUser);
            clearSelectedUser();
        }
    }

    private void clearSelectedUser() {
        selectedUser = null;
        newUser = null;
        users = null; // Reset the users list to fetch the latest data
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public boolean hasSelectedUser() {
    return selectedUser != null;
    }

    public Users getNewUser() {
        return newUser;
    }

    public void setNewUser(Users newUser) {
        this.newUser = newUser;
    }
}

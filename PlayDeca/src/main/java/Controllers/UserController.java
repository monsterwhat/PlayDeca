package Controllers;

import Models.Users;
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
    
    public void InsertFirstUser(){
        if(UserService.count() < 1){
            UserService.InsertAdmin();
        }
    }
    
    public List<Users> getUsers() {
        if (users == null) {
            users = UserService.listAll();
        }
        return users;
    }
    
    public long getUsersCount(){
        return UserService.count();
    }
    
    public void openNewUser() {
        newUser = new Users();
        System.out.println("New User!");
    }
    
    public void saveUser() {
        UserService.update(selectedUser);
        clearSelectedUser();
    }
    
    public void createUser(){
        UserService.create(newUser);
        clearSelectedUser();
    }

    public void deleteUser() {
        if(selectedUser != null){
            UserService.delete(selectedUser);
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

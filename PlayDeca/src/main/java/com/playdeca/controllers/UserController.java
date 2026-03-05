package com.playdeca.controllers;

import com.playdeca.models.Users;
import com.playdeca.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "UserController")
@SessionScoped
public class UserController implements Serializable {

    @Inject
    UserService userService;

    private List<Users> users;
    private Users selectedUser;
    private Users newUser;

    @PostConstruct
    public void init() {
        this.users = userService.getUsers();
    }

    public List<Users> getUsers() {
        if (users == null) {
            users = userService.getUsers();
        }
        return users;
    }

    public int getUsersCount() {
        return userService.getUsers() != null ? userService.getUsers().size() : 0;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Users getNewUser() {
        return newUser;
    }

    public void setNewUser(Users newUser) {
        this.newUser = newUser;
    }

    public void openNewUser() {
        this.newUser = new Users();
    }

    public void saveUser() {
        if (selectedUser != null) {
            userService.update(selectedUser);
            this.users = userService.getUsers();
        }
    }

    public void createUser() {
        if (newUser != null) {
            userService.create(newUser);
            this.users = userService.getUsers();
            this.newUser = null;
        }
    }

    public void deleteUser() {
        if (selectedUser != null) {
            userService.delete(selectedUser);
            this.users = userService.getUsers();
            this.selectedUser = null;
        }
    }

    public void refresh() {
        this.users = userService.getUsers();
    }
}

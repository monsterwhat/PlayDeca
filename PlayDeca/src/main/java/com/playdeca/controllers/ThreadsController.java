package com.playdeca.controllers;

import com.playdeca.models.Threads;
import com.playdeca.models.Users;
import com.playdeca.services.ThreadService;
import com.playdeca.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("ThreadsController")
@SessionScoped
public class ThreadsController implements Serializable {

    @Inject
    ThreadService threadService;

    @Inject
    UserService userService;

    private List<Threads> list;
    private Threads selectedThread;
    private Threads newThread;
    private Long selectedUserId;

    @PostConstruct
    public void init() {
        this.list = threadService.listAll();
    }

    public List<Threads> getList() {
        if (list == null) {
            list = threadService.listAll();
        }
        return list;
    }

    public List<Users> getUsers() {
        return userService.getUsers();
    }

    public Threads getSelectedThread() {
        return selectedThread;
    }

    public void setSelectedThread(Threads selectedThread) {
        this.selectedThread = selectedThread;
    }

    public Threads getNewThread() {
        return newThread;
    }

    public void setNewThread(Threads newThread) {
        this.newThread = newThread;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public void openNewThread() {
        this.newThread = new Threads();
    }

    public void saveThread() {
        if (selectedThread != null && selectedUserId != null) {
            Users user = userService.getUserById(selectedUserId);
            selectedThread.setUser(user);
            threadService.update(selectedThread);
            this.list = threadService.listAll();
        }
    }

    public void createThread() {
        if (newThread != null && selectedUserId != null) {
            Users user = userService.getUserById(selectedUserId);
            newThread.setUser(user);
            threadService.create(newThread);
            this.list = threadService.listAll();
            this.newThread = null;
        }
    }

    public void deleteThread() {
        if (selectedThread != null) {
            threadService.delete(selectedThread);
            this.list = threadService.listAll();
            this.selectedThread = null;
        }
    }

    public void refresh() {
        this.list = threadService.listAll();
    }
}

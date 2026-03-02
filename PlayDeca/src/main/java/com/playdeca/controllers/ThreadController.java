package com.playdeca.controllers;

import com.playdeca.models.Threads;
import com.playdeca.services.ThreadService;
import com.playdeca.services.PostService;
import com.playdeca.models.Posts;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class ThreadController implements Serializable {
    
    @Inject
    private ThreadService threadService;
    
    @Inject
    private PostService postService;
    
    @Inject
    private SessionController sessionController;
    
    private Threads selectedThread;
    private Threads newThread;
    private List<Threads> threadList;
    
    public ThreadController() {
        selectedThread = null;
    }
    
    public List<Threads> getThreadList() {
        if (threadList == null) {
            threadList = threadService.listAll();
        }
        return threadList;
    }
    
    public void refreshThreadList() {
        threadList = threadService.listAll();
    }
    
    public Threads getSelectedThread() {
        return selectedThread;
    }
    
    public void setSelectedThread(Threads thread) {
        this.selectedThread = thread;
    }
    
    public Threads getNewThread() {
        if (newThread == null) {
            newThread = new Threads();
        }
        return newThread;
    }
    
    public void setNewThread(Threads newThread) {
        this.newThread = newThread;
    }
    
    public void selectThread(Threads thread) {
        this.selectedThread = thread;
    }
    
    public List<Posts> getPostsForSelectedThread() {
        if (selectedThread == null) {
            return null;
        }
        return postService.findByThread(selectedThread);
    }
    
    public void createThread() {
        try {
            if (!sessionController.isAdmin()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Only admins can create threads."));
                return;
            }
            
            Threads thread = getNewThread();
            thread.setUser(sessionController.getCurrentUser());
            thread.setDate(java.time.LocalDateTime.now());
            threadService.create(thread);
            
            newThread = new Threads();
            refreshThreadList();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Thread created successfully."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create thread: " + e.getMessage()));
        }
    }
    
    public void deleteThread(Threads thread) {
        try {
            if (!sessionController.isAdmin()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Only admins can delete threads."));
                return;
            }
            
            threadService.delete(thread);
            if (selectedThread != null && selectedThread.equals(thread)) {
                selectedThread = null;
            }
            refreshThreadList();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Thread deleted successfully."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete thread: " + e.getMessage()));
        }
    }
    
    public long getPostCount(Threads thread) {
        return threadService.getPostCount(thread);
    }
}
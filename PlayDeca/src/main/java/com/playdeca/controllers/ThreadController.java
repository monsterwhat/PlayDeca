package com.playdeca.controllers;

import com.playdeca.models.Threads;
import com.playdeca.services.ThreadService;
import com.playdeca.services.PostService;
import com.playdeca.models.Posts;

import jakarta.enterprise.context.RequestScoped;
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
    
    private Threads selectedThread;
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
    
    public Threads getSelectedThread() {
        return selectedThread;
    }
    
    public void setSelectedThread(Threads thread) {
        this.selectedThread = thread;
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
}
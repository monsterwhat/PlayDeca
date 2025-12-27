package com.playdeca.controllers;

import com.playdeca.models.Posts;
import com.playdeca.models.Threads;
import com.playdeca.services.PostService;
import com.playdeca.controllers.ThreadController;
import com.playdeca.controllers.SessionController;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class PostController implements Serializable {
    
    @Inject
    private PostService postService;
    
    @Inject
    private ThreadController threadController;
    
    @Inject
    private SessionController sessionController;
    
    private Posts selectedPost;
    private List<Posts> postList;
    private List<Posts> filteredList;
    
    public PostController() {
        selectedPost = null;
    }
    
    public List<Posts> getList() {
        if (threadController.getSelectedThread() != null) {
            postList = postService.findByThread(threadController.getSelectedThread());
        } else {
            postList = List.of(); // Empty list when no thread is selected
        }
        return postList;
    }
    
    public List<Posts> getFilteredList() {
        return filteredList;
    }
    
    public void setFilteredList(List<Posts> filteredList) {
        this.filteredList = filteredList;
    }
    
    public Posts getSelectedPost() {
        return selectedPost;
    }
    
    public void setSelectedPost(Posts selectedPost) {
        this.selectedPost = selectedPost;
    }
    
    public boolean isModifiable(Posts post) {
        return sessionController.isLoggedIn() && 
               (sessionController.isAdmin() || 
                (post.getUser() != null && post.getUser().equals(sessionController.getCurrentUser())));
    }
    
    public void deletePost() {
        if (selectedPost != null) {
            postService.delete(selectedPost);
            selectedPost = null;
        }
    }
}
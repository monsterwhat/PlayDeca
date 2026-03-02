package com.playdeca.controllers;

import com.playdeca.models.Posts;
import com.playdeca.models.Threads;
import com.playdeca.services.PostService;
import com.playdeca.controllers.ThreadController;
import com.playdeca.controllers.SessionController;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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
    private Posts newPost;
    private List<Posts> postList;
    private List<Posts> filteredList;
    
    public PostController() {
        selectedPost = null;
    }
    
    public List<Posts> getList() {
        if (threadController.getSelectedThread() != null) {
            postList = postService.findByThread(threadController.getSelectedThread());
        } else {
            postList = List.of();
        }
        return postList;
    }
    
    public void refreshPosts() {
        postList = null;
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
    
    public Posts getNewPost() {
        if (newPost == null) {
            newPost = new Posts();
        }
        return newPost;
    }
    
    public void setNewPost(Posts newPost) {
        this.newPost = newPost;
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
            refreshPosts();
        }
    }
    
    public void createPost() {
        try {
            if (!sessionController.isLoggedIn()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to create a post."));
                return;
            }
            
            if (threadController.getSelectedThread() == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No thread selected."));
                return;
            }
            
            Posts post = getNewPost();
            post.setThread(threadController.getSelectedThread());
            post.setUser(sessionController.getCurrentUser());
            post.setDate(java.time.LocalDateTime.now());
            post.setUpvotes(0);
            post.setDownvotes(0);
            postService.create(post);
            
            newPost = new Posts();
            refreshPosts();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Post created successfully."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create post: " + e.getMessage()));
        }
    }
    
    public void savePost() {
        try {
            if (selectedPost == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No post selected."));
                return;
            }
            
            postService.update(selectedPost);
            refreshPosts();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Post saved successfully."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save post: " + e.getMessage()));
        }
    }
    
    public void upvotePost(Posts post) {
        try {
            if (!sessionController.isLoggedIn()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to vote."));
                return;
            }
            
            postService.upvotePost(post, sessionController.getCurrentUser());
            refreshPosts();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to upvote: " + e.getMessage()));
        }
    }
    
    public void downvotePost(Posts post) {
        try {
            if (!sessionController.isLoggedIn()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to vote."));
                return;
            }
            
            postService.downvotePost(post, sessionController.getCurrentUser());
            refreshPosts();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to downvote: " + e.getMessage()));
        }
    }
    
    public int getUserVote(Posts post) {
        if (!sessionController.isLoggedIn()) {
            return 0;
        }
        return postService.getUserVote(post, sessionController.getCurrentUser());
    }
}
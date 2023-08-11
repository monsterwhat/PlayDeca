package Controllers;

import Models.Posts;
import Services.PostService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Al
 */
@Named(value = "PostController")
@ViewScoped
public class PostController implements Serializable{
    
    private List<Posts> cachedPosts;
    private boolean isCacheValid;
    private String UserID = null;
    private Posts selectedPost;
    private Posts newPost = new Posts();
    private List<Posts> filteredList;
    
    @Inject PostService PostService;
    @Inject SessionController SessionController;
    
    public long getPostsCount(){
        return PostService.count();
    }
    
    public PostController() {
        cachedPosts = new ArrayList<>();
        isCacheValid = false;
    }
    
    public List<Posts> getList() {
        if(!isCacheValid){
            cachedPosts = PostService.listAll();
            isCacheValid=true;
        }
        return cachedPosts;
    }
    
    public boolean isModifiable(Posts thePost){
        return SessionController.isAdmin() || thePost.getUser() == SessionController.getCurrentUser();
    }
    
    public void deletePost(){
        if(selectedPost!=null){
            PostService.delete(selectedPost);
            SessionController.getLogger().createLog("Deleted Post", "Successfully deleted Post: "+ selectedPost.getPostId() +"", SessionController.getCurrentUser());
            invalidateCache();
        }
    }
    
    public void createPost(){
        PostService.create(newPost);
        SessionController.getLogger().createLog("Created Post", "Successfully created Post: "+ newPost.getPostId() +"", SessionController.getCurrentUser());
        invalidateCache();
    }
    
    public void savePost(){
        PostService.update(selectedPost);
        SessionController.getLogger().createLog("Updated Post", "Successfully updated Post: "+ selectedPost.getPostId() +"", SessionController.getCurrentUser());
        invalidateCache();
    }
    
    public void openNewPost() {
        newPost = new Posts();
    }
    
    public boolean hasSelectedPost() {
        return selectedPost != null;
    }
    
    private void invalidateCache() {
        isCacheValid = false;
    }

    public Posts getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Posts selectedPost) {
        this.selectedPost = selectedPost;
    }

    public PostService getPostService() {
        return PostService;
    }

    public void setPostService(PostService PostService) {
        this.PostService = PostService;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public List<Posts> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Posts> filteredList) {
        this.filteredList = filteredList;
    }

    public Posts getNewPost() {
        return newPost;
    }

    public void setNewPost(Posts newPost) {
        this.newPost = newPost;
    }
    
}

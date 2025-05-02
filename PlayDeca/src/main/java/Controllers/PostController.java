package Controllers;

import Models.Posts;
import Services.PostService;
import Services.ThreadService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Al
 */
@Named(value = "PostController")
@ViewScoped
@Data
public class PostController implements Serializable{
    
    private List<Posts> cachedPosts;
    private boolean isCacheValid;
    private String UserID = null;
    private Posts selectedPost;
    private Posts newPost = new Posts();
    private List<Posts> filteredList;
    private Long postThreadId;

    @Inject PostService PostService;
    @Inject ThreadService ThreadService;
    @Inject SessionController SessionController;
    @Inject ThreadsController threadController;
    
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
            //ListAllofThread()
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
        if(postThreadId != null || postThreadId != 0){
            newPost.setThread(ThreadService.getThreadByID(postThreadId));
            PostService.create(newPost);
            SessionController.getLogger().createLog("Created Post", "Successfully created Post: "+ newPost.getPostId() +"", SessionController.getCurrentUser());
            invalidateCache();
        }
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
  
}

package Controllers;

import Models.Posts;
import Services.PostService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Al
 */
@Named(value = "PostController")
@ViewScoped
public class PostController implements Serializable{

    public PostController() {
    }
    
    @Inject PostService PostService;
    
    @Inject SessionController SessionController;
    
    
    String UserID = null;
        
    private Posts selectedPost = new Posts();
    
    private Posts newPost = new Posts();
    
    private List<Posts> filteredList;
    
    public List<Posts> getList() {
        List<Posts> List = PostService.listAll();
        return List;
    }
    
    public void deletePost(Posts post){
        PostService.deletePost(post);
    }
    
    public void createPost(){
            PostService.addPost(newPost);
            newPost = new Posts();
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

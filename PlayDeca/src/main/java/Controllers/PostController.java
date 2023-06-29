package Controllers;

import Models.Post;
import Services.PostService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Al
 */
@Named(value = "PostController")
@RequestScoped
public class PostController implements Serializable{

    public PostController() {
    }
    
    @Inject PostService PostService;
    
    List<Post> List = PostService.getAllPosts();

    public List<Post> getList() {
        return List;
    }

    public void setList(List<Post> List) {
        this.List = List;
    }
    
    
    
    
}

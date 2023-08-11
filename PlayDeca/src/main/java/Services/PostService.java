package Services;

import Controllers.SessionController;
import Models.Posts;
import Models.Threads;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.Date;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class PostService extends GService<Posts>{
    
    @Override
    protected Class<Posts> getEntityClass(){
        return Posts.class;
    }
    
    @Inject SessionController SessionController;
    
    @Inject ThreadService ThreadService;
    
    private Threads thread = null;

    public PostService() {
    }
    
    @PostConstruct
    void init(){
    }
    
    public void getThread(){
       thread = ThreadService.getThreadByID(1);
    }
    
    @Override
    public void create(Posts post) {
        try {
            post.setUser(SessionController.getCurrentUser());
            post.setDate(new Date());
            post.setUpvotes(0);
            post.setDownvotes(0);
            post.setThread(thread);
            em.persist(post);
            System.out.println("Post added successfully!");
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error adding post: " + e.toString());
        }
    }
    
}

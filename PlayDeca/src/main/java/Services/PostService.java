package Services;

import Controllers.SessionController;
import Models.Posts;
import Models.Threads;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;

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
    
    public void getThread(){
       thread = ThreadService.getThreadByID(1L);
    }
    
    public List<Posts> listAllofThread(Threads thread){
        
        return null;
        
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
            em.flush();

            System.out.println("Post added successfully!");
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error adding post: " + e.toString());
        }
    }
    
    @Override
    public void delete(Posts post) {
        try {
            if (!em.contains(post)) {
                post = em.find(getEntityClass(), post.getPostId());
            }

            if (post != null) {
                em.remove(post);
                em.flush();

            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    
}

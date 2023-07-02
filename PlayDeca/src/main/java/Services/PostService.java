package Services;

import Controllers.SessionController;
import Models.Posts;
import Models.Threads;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class PostService implements Serializable
{
    @PersistenceContext EntityManager em;
    
    @Inject SessionController SessionController;
    
    @Inject ThreadService ThreadService;
    
    private Threads thread = null;

    public PostService() {
    }
    
    @PostConstruct
    void init(){
    }
    
    public List<Posts> listAll() {
        try {
            TypedQuery<Posts> query = em.createQuery("SELECT p FROM Posts p", Posts.class); 
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public void getThread(){
       thread = ThreadService.getThreadByID(1);
    }
    
    public void addPost(Posts post) {
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
    
    public void deletePost(Posts post){
        try {
                em.remove(post);
        } catch (Exception e) {
            
        }
    }

}
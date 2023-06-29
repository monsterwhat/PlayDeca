package Services;

import Models.Post;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class PostService implements Serializable
{
    @PersistenceContext()
    EntityManager em;
    
    @Resource UserTransaction userTransaction;

    public PostService() {
    }
    
    @PostConstruct
    void init(){
    }
    
    public List<Post> getAllPosts() {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p", Post.class); 
        return query.getResultList();
    }
    
}

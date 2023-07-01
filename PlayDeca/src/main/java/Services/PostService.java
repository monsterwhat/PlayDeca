package Services;

import Models.Posts;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
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
    
    @Transactional
    public List<Posts> listAll() {
        try {
            TypedQuery<Posts> query = em.createQuery("SELECT p FROM Posts p", Posts.class); 
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }
    
    @Transactional
    public void addPost(Posts post){
        try {
            userTransaction.begin();
            em.persist(post);
            userTransaction.commit();

        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException e) {
            System.out.println("Error: " + e.toString());
        }
        
    }
        
}

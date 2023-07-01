package Services;

import Models.Threads;
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
public class ThreadService implements Serializable{
    @PersistenceContext()
    EntityManager em;
    
    @Resource UserTransaction userTransaction;

    public ThreadService() {
    }
    
    @PostConstruct
    void init(){
    }
    
    public List<Threads> listAll(){
        try {
            TypedQuery<Threads> query = em.createQuery("SELECT t FROM Threads t", Threads.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Threads getThreadByID(int threadId){
        try {
            return em.find(Threads.class, threadId);
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            return null;
        }
    }
    
}

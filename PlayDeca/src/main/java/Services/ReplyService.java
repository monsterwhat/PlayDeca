package Services;

import Models.Reply;
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
public class ReplyService implements Serializable{
    @PersistenceContext()
    EntityManager em;
    
    @Resource UserTransaction userTransaction;

    public ReplyService() {
    }
    
    @PostConstruct
    void init(){
    }
    
    public List<Reply> getAllReplies() {
        try {
                TypedQuery<Reply> query = em.createQuery("SELECT r FROM Reply r JOIN FETCH r.user JOIN FETCH r.post", Reply.class);
                return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}

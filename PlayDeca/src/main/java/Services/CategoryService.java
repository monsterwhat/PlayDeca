package Services;

import Models.Category;
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
public class CategoryService implements Serializable{
    @PersistenceContext()
    EntityManager em;
    
    @Resource UserTransaction userTransaction;

    public CategoryService() {
    }
    
    @PostConstruct
    void init(){
    }
    
    public List<Category> listAll(){
        try {
                TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c", Category.class);
                return query.getResultList();
        } catch (Exception e) {
                return null;
        }
    }
    
    
}

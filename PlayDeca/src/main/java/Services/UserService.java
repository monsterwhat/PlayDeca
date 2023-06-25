package Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author Al
 */

public class UserService {
    @PersistenceContext
    EntityManager em;

    public UserService() {
    }
        
    
}

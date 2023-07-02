package Services;

import Models.Users;
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
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class UserService implements Serializable{
    
    @PersistenceContext()
    EntityManager em;
    
    @Resource UserTransaction userTransaction;
    
    public UserService() {
    }
    
    @PostConstruct
    void init(){
        InsertAdmin();
    }
    
    public void InsertAdmin(){  
        try {
            this.userTransaction.begin();
            String username = "Admin";

            TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", username);
            List<Users> existingUsers = query.getResultList();

            if (existingUsers.isEmpty()) {
                Users user = new Users();
                user.setUsername("Admin");
                user.setPassword("password123");
                user.setUUID("some-uuid");
                user.setEmail("admin@playdeca.com");
                user.setRegistrationDate(new Date());
                user.setRole("Administrator");

                em.persist(user);
                this.userTransaction.commit();
                System.out.println("Default Admin Saved!");
            } else {
                System.out.println("User already exists");
                this.userTransaction.rollback();
            }
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException e) {
            System.out.println("Error in InsertAdmin! Error: " + e.toString());
        }

    }
    
    public boolean login(String username, String password) {
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM Users u WHERE u.username = :username AND u.password = :password", Long.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            Long count = query.getSingleResult();

            System.out.println(count);
            return count > 0;
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error: ");
            System.out.println(e);
            return false;
        }
    }
 
    public Users getSession(String username, String password) {
        try {
            TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username AND u.password = :password", Users.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            List<Users> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error: ");
            System.out.println(e);
            return null;
        }
    }

}

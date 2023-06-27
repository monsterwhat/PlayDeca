package Services;

import Models.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
            userTransaction.begin();
            String sqlQuery = "SELECT * FROM user WHERE user.username = ?1";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1,"admin");
            List<User> existingUsers = query.getResultList();

            if (existingUsers.isEmpty()) {
                
            User user = new User();
                user.setUsername("Admin");
                user.setPassword("password123");
                user.setUUID("some-uuid");
                user.setEmail("admin@playdeca.com");
                user.setRegistrationDate(new Date());
        
                em.persist(user);
                userTransaction.commit();
                System.out.println("Default Admin Saved!");
            } else {
                System.out.println("User already exists");
            }
            
            } catch (Exception e) {
                    System.out.println("Error in InsertAdmin! Error: " + e.toString());
            }
    }
    
    public boolean login(String username, String password){
        try {
            String sqlQuery = "SELECT COUNT(*) FROM user WHERE username = ?1 AND password = ?2";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, username);
            query.setParameter(2, password);

            var result = (Number) query.getSingleResult();  // Cast the result to Number type
            int count = result.intValue();
            
            System.out.println(count);
            if(count > 0){
                return true;
            }else{
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Error: ");
            System.out.println(e);
            return false;
        }
    }   
    
    public User getSession(String username, String password){
        try {
        String sqlQuery = "SELECT * FROM user WHERE username = ?1 AND password = ?2";
        Query query = em.createNativeQuery(sqlQuery, User.class);
        query.setParameter(1, username);
        query.setParameter(2, password);

        List<User> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            } else {
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("Error: ");
            System.out.println(e);
            return null;
        }
    }
}

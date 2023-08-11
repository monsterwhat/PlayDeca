package Services;

import Controllers.SessionController;
import Models.Products;
import Models.Users;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
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
public class UserService extends GService<Users>{
    
    @Override
    protected Class<Users> getEntityClass(){
        return Users.class;
    }
    
    @Resource UserTransaction userTransaction;
    
    @Inject SessionController session;
    
    @Inject Pbkdf2PasswordHash passwordHasher;
    
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
                user.setUsername(username);
                user.setPassword(passwordHasher.generate("password123".toCharArray()));
                user.setUUID("e6fc0ebdfd7e4e86ad0ffce099a0a9b4");
                user.setEmail("admin@playdeca.com");
                user.setRegistrationDate(new Date());
                user.setUserGroup("admin");

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
            TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", username);

            List<Users> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                Users user = resultList.get(0);
                String hashedPassword = user.getPassword();

                // Verify the provided password against the hashed password in the database
                if (passwordHasher.verify(password.toCharArray(), hashedPassword)) {
                    return true;
                }
            }

        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error: ");
            System.out.println(e);
            return false;
        }
        return false;
    }
 
    public Users getSession(String username, String password) {
        try {
            TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", username);

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
    
    @Override
    public void create(Users user){
        try {
            user.setRegistrationDate(new Date());
            var unHashedPassword = user.getPassword();
            var HashedPassword = passwordHasher.generate(unHashedPassword.toCharArray());
            user.setPassword(HashedPassword);
            em.persist(user);
            session.getLogger().createLog("Created User", "Successfully created User: "+ user.getUserID() +"", session.getCurrentUser());
        } catch (Exception e) {
        }
    }
    
    public boolean verifyPassword(char[] password, String hashedPassword){
        return passwordHasher.verify(password, hashedPassword);
    }
    
}

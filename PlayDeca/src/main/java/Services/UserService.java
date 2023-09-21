package Services;

import Controllers.SessionController;
import Models.Users;
import Utils.IdentityStoreConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
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
    
    @Inject private IdentityStoreConfig.IdentityStoreConfigProvider ISCP;
    
    public UserService() {
    }
    
    @PostConstruct
    void init(){
        if(count() < 1){
            InsertAdmin();
        }
    }
    
    public Users getUserById(Long userId) {
        try {
            // Create a TypedQuery to fetch the Users object by userID
            TypedQuery<Users> query = em.createQuery(
                "SELECT u FROM Users u WHERE u.userID = :userId", Users.class);

            // Set the userId parameter
            query.setParameter("userId", userId);

            // Execute the query and return the result
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error finding user by id" +e.getLocalizedMessage());
            return null;
        }
    }
    
    public void refreshIdentityStoreConfig() {
        // Recreate or reinitialize the IdentityStoreConfigProvider here
        ISCP = new IdentityStoreConfig.IdentityStoreConfigProvider();
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
                em.flush();

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
 
    public Users getSession(String username) {
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
            em.flush();
            session.getLogger().createLog("Created User", "Successfully created User: "+ user.getUserID() +"", session.getCurrentUser());
           
        } catch (Exception e) {
            
        }
        refreshIdentityStoreConfig();
    }
    
    public void register(Users user){
        try {
            user.setRegistrationDate(new Date());
            var unHashedPassword = user.getPassword();
            var HashedPassword = passwordHasher.generate(unHashedPassword.toCharArray());
            user.setPassword(HashedPassword);
            em.persist(user);
            em.flush();
            session.getLogger().createLog("Created User", "Successfully created User: "+ user.getUserID() +"", user);
           
        } catch (Exception e) {
        }
        refreshIdentityStoreConfig();
    }
    
    @Override
    public void delete(Users user) {
        // Check if the user to delete is the currently logged-in user
        Users loggedInUser = session.getCurrentUser();
        
        if (loggedInUser != null && loggedInUser.getUserID().equals(user.getUserID())) {
            // Invalidate the user's session (log them out)
            session.loglessOut();
        }
        
        try {
            if (!em.contains(user)) {
                user = em.find(getEntityClass(), user.getUserID());
            }

            if (user != null) {
                em.remove(user);
                em.flush();
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        refreshIdentityStoreConfig();
    }
    
    @Override    
    public void update(Users user) {
        try {
            em.merge(user);
            em.flush();

        } catch (Exception e) {
        }
        refreshIdentityStoreConfig();
    }
    
    public boolean verifyPassword(char[] password, String hashedPassword){
        return passwordHasher.verify(password, hashedPassword);
    }
    
    public boolean doesEmailAlreadyExists(String email) {
    TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class);
    query.setParameter("email", email);

    List<Users> resultList = query.getResultList();

    return !resultList.isEmpty();
    }

    public boolean doesUsernameAlreadyExists(String username) {
    TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
    query.setParameter("username", username);

    List<Users> resultList = query.getResultList();

    return !resultList.isEmpty();
    }

    
}

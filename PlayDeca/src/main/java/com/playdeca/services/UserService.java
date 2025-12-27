package com.playdeca.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.playdeca.controllers.SessionController;
import com.playdeca.models.Users;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */

@Named
@ApplicationScoped
public class UserService {
    
    @Inject
    SessionController session;
    
    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (count() == 0) {
            InsertAdmin();
        }
    }
    
    public UserService() {
    } 
    
    public Users getUserById(Long userId) {
        try {
            return Users.findById(userId);
        } catch (Exception e) {
            System.out.println("Error finding user by id" + e.getLocalizedMessage());
            return null;
        }
    }
       
    @Transactional
    public void InsertAdmin() {
        try { 
            String username = "Admin";
            
            List<Users> existingUsers = Users.list("username", username);

            if (existingUsers.isEmpty()) {
                Users user = new Users();
                user.setUsername(username);
                user.setPassword(hashPassword("password123"));
                user.setUUID("e6fc0ebdfd7e4e86ad0ffce099a0a9b4");
                user.setEmail("admin@playdeca.com");
                user.setRegistrationDate(LocalDateTime.now());
                user.setUserGroup("admin");

                user.persist();
 
                System.out.println("Default Admin Saved!");
            } else {
                System.out.println("User already exists"); 
            }
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error in InsertAdmin! Error: " + e.toString());
        }
    }
    
    public boolean login(String username, String password) {
        try {
            List<Users> resultList = Users.list("username", username);

            if (!resultList.isEmpty()) {
                Users user = resultList.get(0);
                String hashedPassword = user.getPassword();

                // Verify the provided password against the hashed password in the database
                if (verifyPassword(password.toCharArray(), hashedPassword)) {
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
            List<Users> resultList = Users.list("username", username);

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
    
    @Transactional
    public void create(Users user) {
        try {
            user.setRegistrationDate(LocalDateTime.now());
            var unHashedPassword = user.getPassword();
            var HashedPassword = hashPassword(unHashedPassword);
            user.setPassword(HashedPassword);
            user.persist();
            
            if (session != null && session.getLogger() != null && session.getCurrentUser() != null) {
                session.getLogger().createLog("Created User", "Successfully created User: " + user.getUserID() + "", session.getCurrentUser());
            }
           
        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        } 
    }
    
    @Transactional
    public void register(Users user) {
        try {
            user.setRegistrationDate(LocalDateTime.now());
            var unHashedPassword = user.getPassword();
            var HashedPassword = hashPassword(unHashedPassword);
            user.setPassword(HashedPassword);
            user.persist();
            
            if (session != null && session.getLogger() != null) {
                session.getLogger().createLog("Created User", "Successfully created User: " + user.getUserID() + "", user);
            }
           
        } catch (Exception e) {
            System.out.println("Error: " + e.getLocalizedMessage());
        } 
    }
    
    @Transactional
    public void delete(Users user) {
        // Check if the user to delete is the currently logged-in user
        Users loggedInUser = session.getCurrentUser();
        
        if (loggedInUser != null && loggedInUser.getUserID().equals(user.getUserID())) {
            // Invalidate the user's session (log them out)
            session.loglessOut();
        }
        
        try {
            Users entity = Users.findById(user.getUserID());
            if (entity != null) {
                entity.delete();
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } 
    }
    
    @Transactional
    public void update(Users user) {
        try {
            Users entity = Users.findById(user.getUserID());
            if (entity != null) {
                entity.setUsername(user.getUsername());
                entity.setEmail(user.getEmail());
                entity.setUUID(user.getUUID());
                entity.setUserGroup(user.getUserGroup());
                entity.setPassword(user.getPassword());
                entity.setRegistrationDate(user.getRegistrationDate());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } 
    }
    
    public boolean verifyPassword(char[] password, String hashedPassword) {
        // BCrypt password verification
        return BCrypt.verifyer().verify(password, hashedPassword).verified;
    }
    
    public String hashPassword(String password) {
        // BCrypt password hashing with automatic salt generation
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    
    public boolean doesEmailAlreadyExists(String email) {
        List<Users> resultList = Users.list("email", email);
        return !resultList.isEmpty();
    }

    public boolean doesUsernameAlreadyExists(String username) {
        List<Users> resultList = Users.list("username", username);
        return !resultList.isEmpty();
    }
    
    public long count() {
        return Users.count();
    }
}
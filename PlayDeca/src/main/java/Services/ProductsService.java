package Services;

import Models.Products;
import Models.Users;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class ProductsService implements Serializable{
        @PersistenceContext EntityManager em;

        public List<Products> listAll() {
        try {
            TypedQuery<Products> query = em.createQuery("SELECT p FROM Products p", Products.class); 
            return query.getResultList();
            } catch (Exception e) {
                return null;
            }
        }
        
    public void updateProduct(Products product) {
        try {
            em.merge(product);
            //session.getLogger().createLog("Updated User", "Successfully updated User: "+ user.getUserID() +"", session.getCurrentUser());
        } catch (Exception e) {
        }
    }
    
    public void createProduct(Products product){
        try {
            em.persist(product);
            //session.getLogger().createLog("Created User", "Successfully created User: "+ user.getUserID() +"", session.getCurrentUser());
        } catch (Exception e) {
        }
    }
    
    public void deleteProduct(Products product){
        try {
            if (!em.contains(product)) {
                // Entity is detached, obtain a managed instance
                product = em.find(Products.class, product.getProductId());
            }

            if (product != null) {
                em.remove(product);
               //session.getLogger().createLog("Deleted User", "Successfully deleted User: "+ user.getUserID() +"", session.getCurrentUser());
            } else {
                System.out.println("Post not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
        
}

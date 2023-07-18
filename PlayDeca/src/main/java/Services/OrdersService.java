package Services;

import Models.Orders;
import Models.Products;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Al
 */

@Named
@Transactional
public class OrdersService implements Serializable{
        @PersistenceContext EntityManager em;
        
        public List<Orders> listAll() {
        try {
            TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o", Orders.class); 
            return query.getResultList();
            } catch (Exception e) {
                return null;
            }
        }
        
    public void updateOrder(Orders order) {
        try {
            em.merge(order);
            //session.getLogger().createLog("Updated User", "Successfully updated User: "+ user.getUserID() +"", session.getCurrentUser());
        } catch (Exception e) {
        }
    }
    
    public void createOrder(Orders order){
        try {
            em.persist(order);
            //session.getLogger().createLog("Created User", "Successfully created User: "+ user.getUserID() +"", session.getCurrentUser());
        } catch (Exception e) {
        }
    }
    
    public void deleteOrder(Orders order){
        try {
            if (!em.contains(order)) {
                // Entity is detached, obtain a managed instance
                order = em.find(Orders.class, order.getOrderId());
            }

            if (order != null) {
                em.remove(order);
               //session.getLogger().createLog("Deleted User", "Successfully deleted User: "+ user.getUserID() +"", session.getCurrentUser());
            } else {
                System.out.println("Post not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}

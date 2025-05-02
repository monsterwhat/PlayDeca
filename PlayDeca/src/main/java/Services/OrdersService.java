package Services;

import Models.Orders;
import jakarta.ejb.Stateless;
import jakarta.inject.Named; 

/**
 *
 * @author Al
 */

@Named
@Stateless
public class OrdersService extends GService<Orders>{
        
    @Override
    protected Class<Orders> getEntityClass(){
        return Orders.class;
    }
    
    @Override
    public void delete(Orders order) {
        try {
            if (!em.contains(order)) {
                order = em.find(getEntityClass(), order.getOrderId());
            }

            if (order != null) {
                em.remove(order);
                em.flush();

            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    
}

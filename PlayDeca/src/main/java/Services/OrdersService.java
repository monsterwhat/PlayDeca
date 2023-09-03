package Services;

import Models.Orders;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

/**
 *
 * @author Al
 */

@Named
@Transactional
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
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    
}

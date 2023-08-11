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
    
}

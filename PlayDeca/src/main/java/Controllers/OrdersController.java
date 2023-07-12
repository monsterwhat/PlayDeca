package Controllers;

import Models.Orders;
import Services.OrdersService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Al
 */
@Named(value="OrdersController")
@ViewScoped
public class OrdersController implements Serializable {
    
    @Inject private OrdersService ordersService;
    private List<Orders> cachedOrders;
    private Orders selectedOrder;
    private Orders newOrder = new Orders();
    boolean isCacheValid;
    
    public List<Orders> getList() {
        if(!isCacheValid){
            cachedOrders = ordersService.listAll();
            isCacheValid = true;
        }
        return cachedOrders;
    }
    
    private void invalidateCache() {
        isCacheValid = false;
    }

    public OrdersController() {
        cachedOrders = new ArrayList<>();
        isCacheValid = false;
    }
    
    public void deleteOrder(){
        if(selectedOrder!=null){
            ordersService.deleteOrder(selectedOrder);
            //SessionController.getLogger().createLog("Deleted Post", "Successfully deleted Post: "+ selectedPost.getPostId() +"", SessionController.getCurrentUser());
            invalidateCache();
        }
    }
    
    public void createOrder(){
        ordersService.createOrder(newOrder);
        //SessionController.getLogger().createLog("Created Post", "Successfully created Post: "+ newPost.getPostId() +"", SessionController.getCurrentUser());
        invalidateCache();
    }
    
    public void saveOrder(){
        ordersService.updateOrder(selectedOrder);
        //SessionController.getLogger().createLog("Updated Post", "Successfully updated Post: "+ selectedPost.getPostId() +"", SessionController.getCurrentUser());
        invalidateCache();
    }
    
    public void openNewOrder() {
        newOrder = new Orders();
    }
    
    public boolean hasSelectedOrder() {
        return selectedOrder != null;
    }

    public OrdersService getOrdersService() {
        return ordersService;
    }

    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    public List<Orders> getCachedOrders() {
        return cachedOrders;
    }

    public void setCachedOrders(List<Orders> cachedOrders) {
        this.cachedOrders = cachedOrders;
    }

    public Orders getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Orders selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public Orders getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Orders newOrder) {
        this.newOrder = newOrder;
    }

    public boolean isIsCacheValid() {
        return isCacheValid;
    }

    public void setIsCacheValid(boolean isCacheValid) {
        this.isCacheValid = isCacheValid;
    }
    
    
    
}

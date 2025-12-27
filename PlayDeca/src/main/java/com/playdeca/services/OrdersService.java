package com.playdeca.services;

import com.playdeca.models.Orders;
import com.playdeca.models.Products;
import com.playdeca.models.Users;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
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
public class OrdersService {
    
    @Transactional
    public void create(Orders order) {
        try {
            order.setOrderDate(LocalDateTime.now());
            order.persist();
        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }
    
    @Transactional
    public void update(Orders order) {
        try {
            Orders existing = Orders.findById(order.getOrderId());
            if (existing != null) {
                existing.setUser(order.getUser());
                existing.setProducts(order.getProducts());
                existing.setOrderDate(order.getOrderDate());
            }
        } catch (Exception e) {
            System.out.println("Error updating order: " + e.getMessage());
        }
    }
    
    @Transactional
    public void delete(Orders order) {
        try {
            Orders entity = Orders.findById(order.getOrderId());
            if (entity != null) {
                entity.delete();
            }
        } catch (Exception e) {
            System.out.println("Error deleting order: " + e.getMessage());
        }
    }
    
    public List<Orders> listAll() {
        return Orders.listAll();
    }
    
    public Orders findById(Long id) {
        return Orders.findById(id);
    }
    
    public long count() {
        return Orders.count();
    }
}
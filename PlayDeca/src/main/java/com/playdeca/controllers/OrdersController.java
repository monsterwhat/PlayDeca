package com.playdeca.controllers;

import com.playdeca.models.Orders;
import com.playdeca.models.Users;
import com.playdeca.services.OrdersService;
import com.playdeca.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("OrdersController")
@SessionScoped
public class OrdersController implements Serializable {

    @Inject
    OrdersService ordersService;

    @Inject
    UserService userService;

    private List<Orders> list;
    private Orders selectedOrder;
    private Orders newOrder;

    @PostConstruct
    public void init() {
        this.list = ordersService.listAll();
    }

    public List<Orders> getList() {
        if (list == null) {
            list = ordersService.listAll();
        }
        return list;
    }

    public int getOrdersCount() {
        return (int) ordersService.count();
    }

    public List<Users> getUsers() {
        return userService.getUsers();
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

    public void openNewOrder() {
        this.newOrder = new Orders();
    }

    public void saveOrder() {
        if (selectedOrder != null) {
            ordersService.update(selectedOrder);
            this.list = ordersService.listAll();
        }
    }

    public void createOrder() {
        if (newOrder != null) {
            ordersService.create(newOrder);
            this.list = ordersService.listAll();
            this.newOrder = null;
        }
    }

    public void deleteOrder() {
        if (selectedOrder != null) {
            ordersService.delete(selectedOrder);
            this.list = ordersService.listAll();
            this.selectedOrder = null;
        }
    }

    public void refresh() {
        this.list = ordersService.listAll();
    }
}

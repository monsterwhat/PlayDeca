package com.playdeca.controllers;

import com.playdeca.models.Products;
import com.playdeca.services.ProductsService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("ProductsController")
@SessionScoped
public class ProductsController implements Serializable {

    @Inject
    ProductsService productsService;

    private List<Products> list;
    private Products selectedProduct;
    private Products newProduct;

    @PostConstruct
    public void init() {
        this.list = productsService.listAll();
    }

    public List<Products> getList() {
        if (list == null) {
            list = productsService.listAll();
        }
        return list;
    }

    public int getProductsCount() {
        return (int) productsService.count();
    }

    public Products getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Products getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Products newProduct) {
        this.newProduct = newProduct;
    }

    public void openNewProduct() {
        this.newProduct = new Products();
    }

    public void saveProduct() {
        if (selectedProduct != null) {
            productsService.update(selectedProduct);
            this.list = productsService.listAll();
        }
    }

    public void createProduct() {
        if (newProduct != null) {
            productsService.create(newProduct);
            this.list = productsService.listAll();
            this.newProduct = null;
        }
    }

    public void deleteProduct() {
        if (selectedProduct != null) {
            productsService.delete(selectedProduct);
            this.list = productsService.listAll();
            this.selectedProduct = null;
        }
    }

    public void refresh() {
        this.list = productsService.listAll();
    }
}

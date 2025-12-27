package com.playdeca.services;

import com.playdeca.models.Products;
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
public class ProductsService {
    
    @Transactional
    public void create(Products product) {
        try {
            product.setCreatedDate(LocalDateTime.now());
            product.setModifiedDate(LocalDateTime.now());
            product.persist();
        } catch (Exception e) {
            System.out.println("Error creating product: " + e.getMessage());
        }
    }
    
    @Transactional
    public void update(Products product) {
        try {
            Products existing = Products.findById(product.getProductId());
            if (existing != null) {
                existing.setName(product.getName());
                existing.setDescription(product.getDescription());
                existing.setPrice(product.getPrice());
                existing.setCategory(product.getCategory());
                existing.setImageUrl(product.getImageUrl());
                existing.setDiscountPercentage(product.getDiscountPercentage());
                existing.setModifiedDate(LocalDateTime.now());
            }
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }
    
    @Transactional
    public void delete(Products product) {
        try {
            Products entity = Products.findById(product.getProductId());
            if (entity != null) {
                entity.delete();
            }
        } catch (Exception e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
    
    public List<Products> listAll() {
        return Products.listAll();
    }
    
    public Products findById(Long id) {
        return Products.findById(id);
    }
    
    public long count() {
        return Products.count();
    }
}
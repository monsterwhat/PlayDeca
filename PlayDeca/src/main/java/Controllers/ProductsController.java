package Controllers;

import Models.Products;
import Services.ProductsService;
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

@Named(value="ProductsController")
@ViewScoped
public class ProductsController implements Serializable{
    
    @Inject private ProductsService productsService;
    private List<Products> cachedProducts;
    private Products selectedProduct;
    private Products newProduct = new Products();
    boolean isCacheValid;

    public ProductsController() {
        cachedProducts = new ArrayList<>();
        isCacheValid = false;
    }
    
    public List<Products> getList() {
        if(!isCacheValid){
            cachedProducts = productsService.listAll();
            isCacheValid=true;
        }
        return cachedProducts;
    }
    
    private void invalidateCache() {
        isCacheValid = false;
    }
    
    public long getProductsCount(){
        return productsService.productCount();
    }
    
    public void deleteProduct(){
        if(selectedProduct !=null){
            productsService.deleteProduct(selectedProduct);
            invalidateCache();
        }
    }
        
    public void createProduct(){
        productsService.createProduct(newProduct);
        invalidateCache();
    }
        
    public void saveProduct(){
        productsService.updateProduct(selectedProduct);
        invalidateCache();
    }
    
    public void openNewProduct(){
        newProduct = new Products();
    }
    
    public boolean hasSelectedProduct() {
        return selectedProduct != null;
    }

    public ProductsService getProductsService() {
        return productsService;
    }

    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    public List<Products> getCachedProducts() {
        return cachedProducts;
    }

    public void setCachedProducts(List<Products> cachedProducts) {
        this.cachedProducts = cachedProducts;
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

    public boolean isIsCacheValid() {
        return isCacheValid;
    }

    public void setIsCacheValid(boolean isCacheValid) {
        this.isCacheValid = isCacheValid;
    }
       
    
    
}

package Controllers;

import Models.Products;
import Services.ProductsService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Al
 */

@Named(value="ProductsController")
@ViewScoped
@Data
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
        return productsService.count();
    }
    
    public void deleteProduct(){
        if(selectedProduct !=null){
            productsService.delete(selectedProduct);
            invalidateCache();
        }
    }
        
    public void createProduct(){
        productsService.create(newProduct);
        invalidateCache();
    }
        
    public void saveProduct(){
        productsService.update(selectedProduct);
        invalidateCache();
    }
    
    public void openNewProduct(){
        newProduct = new Products();
    }
    
    public boolean hasSelectedProduct() {
        return selectedProduct != null;
    }
  
}

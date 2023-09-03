package Services;

import Models.Products;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class ProductsService extends GService<Products>{
    
    @Override
    protected Class<Products> getEntityClass(){
        return Products.class;
    }
    
    @Override
    public void delete(Products product) {
        try {
            if (!em.contains(product)) {
                product = em.find(getEntityClass(), product.getProductId());
            }

            if (product != null) {
                em.remove(product);
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    
}

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
    
    
}

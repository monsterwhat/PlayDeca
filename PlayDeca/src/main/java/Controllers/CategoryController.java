package Controllers;

import Services.CategoryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Al
 */
@Named(value = "CategoryController")
@RequestScoped
public class CategoryController implements Serializable{

    public CategoryController() {
    }
    
    @Inject CategoryService CategoryService;
    
}

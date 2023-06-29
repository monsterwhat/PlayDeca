package Controllers;

import Services.ReplyService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Al
 */
@Named(value = "ReplyController")
@RequestScoped
public class ReplyController implements Serializable{

    public ReplyController() {
    }
    
    @Inject ReplyService ReplyService;
    
}

package Utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.inject.Named;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import java.io.Serializable;

/**
 *
 * @author Al
 */

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
        loginPage = "/login",
        errorPage = "",
        useForwardToLogin = false)
)
@FacesConfig
@Named
@ApplicationScoped
public class ApplicationConfig implements Serializable{

    public ApplicationConfig() {
    }
    
    

}

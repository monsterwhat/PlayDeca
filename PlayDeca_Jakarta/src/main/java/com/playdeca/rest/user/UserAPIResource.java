package com.playdeca.rest.user;

import com.playdeca.models.JWT_blacklist;
import com.playdeca.models.UserData;
import com.playdeca.services.ValidationService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;

/**
 * REST Web Service
 *
 * @author alvaro@playdeca.com
 */
public class UserAPIResource {

    private String id;
    private UserData user;
    private JWT_blacklist jwt_black;
    private ValidationService validate;

    private UserAPIResource(String id) {
        this.id = id;
    }

    public static UserAPIResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of UserAPIResource class.
        return new UserAPIResource(id);
    }

    @GET
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public UserData getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @PUT
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public void putJson(UserData content) {
    }

    @DELETE
    public void delete() {
    }
}

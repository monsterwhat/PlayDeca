package com.playdeca.rest.admin;

import com.playdeca.models.UserData;
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
public class AdminResource {

    private String id;

    private AdminResource(String id) {
        this.id = id;
    }

    public static AdminResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of AdminResource class.
        return new AdminResource(id);
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

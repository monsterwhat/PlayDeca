package com.playdeca.rest.forum;

import com.playdeca.models.ForumData;
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
public class ForumResource {

    private String id;

    private ForumResource(String id) {
        this.id = id;
    }

    public static ForumResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of ForumResource class.
        return new ForumResource(id);
    }

    @GET
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public ForumData getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @PUT
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public void putJson(ForumData content) {
    }

    @DELETE
    public void delete() {
    }
}

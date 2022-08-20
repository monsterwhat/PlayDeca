package com.playdeca.rest.news;

import com.playdeca.models.NewsData;
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
public class NewsResource {

    private String id;

    private NewsResource(String id) {
        this.id = id;
    }

    public static NewsResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of NewsResource class.
        return new NewsResource(id);
    }

    @GET
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public NewsData getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @PUT
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public void putJson(NewsData content) {
    }

    @DELETE
    public void delete() {
    }
}

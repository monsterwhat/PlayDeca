package com.playdeca.rest.news;

import com.playdeca.models.NewsData;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author alvaro@playdeca.com
 */
@Path("/news")
public class NewssResource {

    @Context
    private UriInfo context;

    public NewssResource() {
    }

    @GET
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public NewsData getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response postJson(NewsData content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    @Path("{id}")
    public NewsResource getNewsResource(@PathParam("id") String id) {
        return NewsResource.getInstance(id);
    }
}

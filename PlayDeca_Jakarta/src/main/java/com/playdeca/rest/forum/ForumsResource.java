package com.playdeca.rest.forum;

import com.playdeca.models.ForumData;
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
@Path("/forum")
public class ForumsResource {

    @Context
    private UriInfo context;

    public ForumsResource() {
    }

    @GET
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public ForumData getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response postJson(ForumData content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    @Path("{id}")
    public ForumResource getForumResource(@PathParam("id") String id) {
        return ForumResource.getInstance(id);
    }
    
    @Path("{create}")
    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response postcreateJson(ForumData content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }
    
    @Path("{delete_post}")
    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response postdeleteJson(ForumData content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }
}

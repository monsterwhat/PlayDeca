package com.playdeca.rest.user;

import com.playdeca.models.UserData;
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
@Path("/user")
public class UserAPIsResource {

    @Context
    private UriInfo context;

    public UserAPIsResource() {
    }

    @GET
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public UserData getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response postJson(UserData content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    @Path("{id}")
    public UserAPIResource getUserAPIResource(@PathParam("id") String id) {
        return UserAPIResource.getInstance(id);
    }
}

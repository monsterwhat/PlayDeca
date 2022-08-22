package com.playdeca.rest.api;

import com.playdeca.dao.UserDataDAOImpl;
import com.playdeca.models.UserData;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Email;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriBuilderException;
import java.net.URI;

/**
 *
 * @author alvaro@playdeca.com
 */
@ApplicationPath("/api")

public class RESTAPI extends Application {

    @Email
    public boolean doesEmailExist(String email) {

        return false;
    }

    @Email
    public boolean isEmailConfirmed(String email) {

        return false;
    }

    
    public boolean validateNewUser(String email) {

        return false;
    }

    public boolean validateSignIn(String User, String password) {

        return false;
    }

    @Path("user")
    @Singleton
    @Stateless
    public class userResource {
        
        @Inject
        private UserDataDAOImpl userController;
        

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/user-info")
        public Response user_info(UserData User){
            
            if(!userController.findByID(User)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if(!userController.seeInfo(User)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            UserData UserInfo = userController.getInfo(User);
            
            return Response.ok(UserInfo, MediaType.APPLICATION_JSON).build();
        }
        
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/all-users")
        public Response all_users(){
            try {
                return Response.ok(userController.findAllPublic()).build();
            } catch (Exception e) {
                
            }
            return null;
        }
        
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/user")
        public Response user(int id){
            
//            try {
//                if(!request.authenticate(response)){
//                    return Response.status(Response.Status.CONFLICT).build();                }
//                if(request.getHeader("banned").equals(true)){
//                    Response.ok("",MediaType.APPLICATION_JSON).build();
//                }
//
//            } catch (ServletException | IOException e) {
//            } finally {
//            }
            return null;

        }
        
        
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/register")
        public Response register(UserData user) {
            try {
                if(userController.exists(user.getEmail())){
                    return Response.status(Response.Status.CONFLICT).build();
                }
                userController.register(user);
                URI location = UriBuilder.fromResource(userResource.class)
                        .path("/{user}")
                        .resolveTemplate("user", user.getEmail())
                        .build();
                return Response.created(location).build();

            } catch (UriBuilderException | IllegalArgumentException e) {
                
                return Response.serverError().build();

            }
        }

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/login")
        public Response loginUser(UserData user) {
            
            if (userController.signIn(user)==true) {
                
                if (userController.findByEmail(user.getEmail())){
                    
                    if( userController.isBanned(user.getExtra_info()) || !userController.isConfirmed(user.getEmail())){
                        return Response.status(Response.Status.FORBIDDEN).build();
                    }
                    else{
                        //CREATE COOKIE AND SEND IT
                        //return Response.accepted("TOKEN");
                    }
                }
            }
        return null;
    }

    @Path("/news")
    public class newsResource {

    }

    @Path("/forum")
    public class forumResource {

    }

    @Path("/admin")
    public class adminResource {

    }
}
}

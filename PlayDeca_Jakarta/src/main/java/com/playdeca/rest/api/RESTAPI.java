package com.playdeca.rest.api;

import com.playdeca.models.UserData;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author alvaro@playdeca.com
 */
@ApplicationPath("/api")
public class RESTAPI extends Application {

    public boolean doesEmailExist(Cookie email) {

        return false;
    }

    public boolean isEmailConfirmed(Cookie email) {

        return false;

    }

    public boolean validateNewUser() {

        return false;

    }
    
    public boolean validateSignIn(String User, String password){
        
        return false;
        
    }

    @Path("/user")
    public class userResource {

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public UserData getUser() {
            return null; // some way to return a Person instance
        }

        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/register")
        public Response.ResponseBuilder register(Cookie user) {
            try {
                
                final String value = user.getValue();

                if (doesEmailExist(user)) {
                    return Response.serverError().status(400,"User is already registered on this Email Address");
                }
                if (validateNewUser()) {

                    UserData newUser = new UserData();

                    newUser.setEmail(value);
                    newUser.setUsername(value);
                    newUser.setPassword(value);
                    newUser.setExtra_info(value);
                    newUser.setDate(Date.from(Instant.now()));
                    newUser.setConfirmed_game(false);
                    newUser.setConfirmed_game(false);
                    newUser.setRole("user");

                    //ADD newUser to Database
                    return Response.ok().status(201);
                    
                }

            } catch (Exception e) {

                return Response.serverError().status(400,"Information was Invalid");
                
            } finally {

            }
            return Response.status(400,"Information was Invalid");
        }
        
        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/login")
        public Response.ResponseBuilder loginUser(Cookie User){
            final String value = User.getValue();

            if(validateSignIn(value, value)){
            
        }
            
            return null;
            
        }
        

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

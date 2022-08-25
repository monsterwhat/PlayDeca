/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playdeca.rest.api;

import com.playdeca.dao.UserDataDAOImpl;
import com.playdeca.models.UserData;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriBuilderException;
import jakarta.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

/**
 *
 * @author alvaro@playdeca.com
 */
@Provider
@Path("/user")
public class UserDataResource {

    @Inject
    private UserDataDAOImpl userController;

    @Context
    private HttpHeaders headers;

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    public void isEmailConfirmed() {

    }

    public void isGameNameConfirmed() {

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/user-info")
    public Response user_info(UserData User) {

        if (!userController.findByID(User)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!userController.seeInfo(User)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UserData UserInfo = userController.getInfo(User);

        return Response.ok(UserInfo, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all-users")
    public Response getUsers() {
        try {
            List<UserData> list = userController.listAll();
            return Response.ok().status(Response.Status.OK).entity(list).build();
        } catch (Exception e) {

        }
        return Response.serverError().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user")
    public Response user(@PathParam("id") Integer id) {
        try {
            UserData user = userController.findByID(id);
            return Response.ok().status(Response.Status.OK).entity(user).build();
        } catch (Exception e) {
            return Response.serverError().build();

        }

//            try {
//                if(!request.authenticate(response)){
//                    return Response.status(Response.Status.CONFLICT).build();                }
//                if(request.getHeader("banned").equals(true)){
//                    Response.ok("",MediaType.APPLICATION_JSON).build();
//                }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response register(@FormParam("email")String email,@FormParam("username")String username,@FormParam("password")String password, UserData user) {
        try {
            if (userController.exists(user.getEmail())) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            UserData newUser = userController.register(user);
            URI location = UriBuilder.fromResource(UserDataResource.class)
                    .path("/{user}")
                    .resolveTemplate("user", newUser.getEmail())
                    .build();
            return Response.ok().status(Response.Status.CREATED).entity(newUser).build();

        } catch (UriBuilderException | IllegalArgumentException e) {

            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response loginUser(@FormParam("email") String email,
            @FormParam("password") String password,
            UserData user) {
        try {
            UserData loggedUser = userController.signIn(email, password);
            if (!loggedUser.equals(null)) {
                if (userController.isBanned() || !userController.isConfirmed(user)) {
                    return Response.status(400,"User is banned").build();
                } else {
                    //CREATE COOKIE AND SEND IT
                    //return Response.accepted("TOKEN");
                    //return Response.ok().status(200).entity(cookie).build();
                }
            }
            return Response.status(400,"Incorrect email or password").build();

        } catch (Exception e) {
            return Response.serverError().status(400, "Invalid information received").build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Response logoutUser() {

        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/check-email")
    public Response checkEmail(@FormParam("email") String email) {
        try {
            UserData foundUser = userController.findByEmail(email);
            if (!foundUser.equals(null)) {
                if (!foundUser.isBanned()) {
                    if (foundUser.isConfirmed_email()) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    } else {
                        userController.confirmEmail(foundUser);
                        return Response.status(Response.Status.FOUND).build();
                    }
                }
            }
            return Response.status(200,"Email format is invalid").status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/confirm-email")
    public Response confirmEmail(@FormParam("password") String password,
            @FormParam("id") int id,
            @FormParam("information") String info) {
        try {
            UserData foundUser = new UserData();
            foundUser = userController.findByID(id);
            if (userController.isBanned(foundUser)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (userController.confirmEmail(foundUser)) {
                return Response.ok().status(200,"Your account\'s email address has been confirmed").status(Response.Status.ACCEPTED).build();
            }
            return Response.status(401,"Unable to confirm email address sorry").status(Response.Status.NOT_ACCEPTABLE).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reset-pass")
    public Response resetPassword(@FormParam("password") String password,
            @FormParam("id") int userId,
            @FormParam("information") String extraInfo) {

        try {
            UserData foundUser = new UserData();
            foundUser = userController.findByID(userId);

            if (!foundUser.isBanned()) {
                if (foundUser.getExtra_info().equals(extraInfo)) {
                    userController.updatePassword(password);
                    return Response.accepted().status(Response.Status.CREATED).build();
                }
                return Response.serverError().status(404,"Incorrect temporary passcode").build();
            }

            return Response.notAcceptable(null).status(Response.Status.FORBIDDEN).build();

        } catch (Exception e) {
            return Response.serverError().status(404,"err").status(Response.Status.NOT_ACCEPTABLE).build();
        }

    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package com.playdeca.rest.user;

import com.playdeca.models.UserData;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author alvaro@playdeca.com
 */
@Path("/user")
public class UsersResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UsersResource
     */
    public UsersResource() {
    }

    @POST
    @Path("/register")
    public Response registerUser(UserData registeredUser){
        
        // doesEmailExist
        // IF doesEmailExiste then return status 400.send(user already registered)
        
        //if validation.validateNewUser then -> new UserData()
        //User -> req.body -> email
        //                 -> username
        //                 -> password (hashed)
        //                 -> extra_info (hashed password email)
        //                 -> creation date
        //                 -> confirmed_game:false
        //                 -> confirmed_email:false
        //                 -> role:"User"
            //try
            // ADD user to DB;
            // if error send 401

        // else send 400 invalid info
        return null;
    }
    @POST
    @Path("/login")
    public Response loginUser(UserData User){
        
        //Using Email and password
        //If validation.validateSignIn(email,password){
            // User.findOne(email), (error, user) => {
                //if error return status 400 Error logging in
                //if !user return stats 400 Incorrect
                //if user.banned return 400 user is banned
                //if !user.confirmed_email send 400 Email not confirmed
            //else
                // token -> jwt.sign(user_id), secret{
                // expires 10000
            // res.cookie('jwt' token, httpOnly true secure false.
            // res.status(200).send token
        
        return null;
    }
    
    @POST
    @Path("/user-info")
    public Response userInfo(UserData User){
        //if body.id => User.findByID(body.id) , (error, user) =>
            //if !error
                //userInformation =
                // id = body.id
                // username = user.username
                // response status 200.json (userInformation);
            //else console.log(error getting Username error= +error
                // res.status(400).jason{message:ErrorFindingID}
        //catch (err) => res.status(400).json(message"User already signed out)
        
        //else
        //res.status(401).json(msg:No User)
        
        return null;
        
    }
    
    @GET
    @Path("/all-users")
    public Response allUsers(UserData User){
        
        
        
        return null;
        
        
    }
}

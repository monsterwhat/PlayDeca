package com.playdeca.models;

import java.util.Date;

public class UserData {
    
    private String email;
    private String username;
    //This is a direct translation, storing this is a massive security risk - Even in memory.
    private String password;
    private Date date;
    private String extra_info;
    private boolean allow_reset;
    private boolean confirmed_game;
    private boolean confirmed_email;
    private String role;
    private boolean banned;
}

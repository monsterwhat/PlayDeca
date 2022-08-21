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
    
        //Jakarta 9 JSON Files requiere NO CONTRUCTORS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public boolean isAllow_reset() {
        return allow_reset;
    }

    public void setAllow_reset(boolean allow_reset) {
        this.allow_reset = allow_reset;
    }

    public boolean isConfirmed_game() {
        return confirmed_game;
    }

    public void setConfirmed_game(boolean confirmed_game) {
        this.confirmed_game = confirmed_game;
    }

    public boolean isConfirmed_email() {
        return confirmed_email;
    }

    public void setConfirmed_email(boolean confirmed_email) {
        this.confirmed_email = confirmed_email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    
}

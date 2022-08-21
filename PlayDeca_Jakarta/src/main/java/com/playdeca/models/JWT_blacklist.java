package com.playdeca.models;

import java.util.Date;

public class JWT_blacklist {
   private String jwt;
   private Date date_added;

       //Jakarta 9 JSON Files requiere NO CONTRUCTORS

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    
   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playdeca.services.Databases;

import java.sql.Connection;

/**
 *
 * @author alvaro@playdeca.com
 */
public class MySQLConfig {
    
    protected Connection newConnection = null;
    private String host = "localhost";
    private String port = "3306";
    private String dbName = "";
    private String user = "";
    private String pass = "";

    public Connection getNewConnection() {
        return newConnection;
    }

    public void setNewConnection(Connection newConnection) {
        this.newConnection = newConnection;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playdeca.services.Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author alvaro@playdeca.com
 */
public class MySQL {

    MySQLConfig config = new MySQLConfig();
    Connection newConnection = config.getNewConnection();

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            newConnection = DriverManager.getConnection("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDbName() + "?serverTimezone=UTC", config.getUser(), config.getPass());
        } catch (ClassNotFoundException | SQLException e) {
            Disconnect();
        }
    }

    public void closeStatement(Statement statement) {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
                statement = null;
            }
        } catch (Exception e) {
        }
    }

    public void closeResultSet(ResultSet resultset) {
        {
            {
                try {
                    if (resultset != null && !resultset.isClosed()) {
                        resultset.close();
                        resultset = null;
                    }
                } catch (SQLException e) {
                    System.out.println("Error cerrando el ResultSet! " + e);
                }
            }
        }
    }

    public void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
                preparedStatement = null;
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar el preparedStatement! " + e);
        }
    }

    public void Disconnect() {
        try {
            if (newConnection != null && !newConnection.isClosed()) {
                newConnection.close();
                newConnection = null;
            }
        } catch (SQLException e) {
            System.out.println("Error al tratar de cerrar la conecion! " + e);
        }
    }

}

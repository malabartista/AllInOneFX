package com.allinonefx.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHandler extends Configs {

    protected Connection dbconnection;

    public Connection getConnection() {
        final String ConnectionString = "jdbc:mysql://" + Configs.dbhost + ":" + Configs.dbport + "/" + Configs.dbname;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try {
            dbconnection = DriverManager.getConnection(ConnectionString, Configs.dbuser, Configs.dbpass);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return dbconnection;
    }

}

package com.allinonefx.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHandler extends Configs {

    protected Connection dbconnection;

    public Connection getConnection() {
        // mysql
        final String ConnectionString = "jdbc:mysql://" + Configs.dbhost + ":" + Configs.dbport + "/" + Configs.dbname;
        // mssql
        final String connectionUrl = "jdbc:jtds:sqlserver://localhost/bdVersion5xp3;user=gestionTrafico;password=gestionTrafico1;instance=sqlexpress01;domain=";
        try {
            // mysql
            Class.forName("com.mysql.jdbc.Driver");
            // mssql
            //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try {
            // mysql
            dbconnection = DriverManager.getConnection(ConnectionString, Configs.dbuser, Configs.dbpass);
            // mssql
            //dbconnection = DriverManager.getConnection(connectionUrl);  
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return dbconnection;
    }

}

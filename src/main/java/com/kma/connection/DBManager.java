
package com.kma.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String url = "jdbc:mysql://localhost:3306/cntt_kma";
    private static final String username = "root";
    private static final String passwd = "12345A@a";

    private static DBManager instance;

    private static Connection connect = null;

    private DBManager() {
        
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connect == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connect = DriverManager.getConnection(url, username, passwd);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }
}


package com.carrental;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Simple JDBC connection helper. Uses SQLite file in project root for portability.
public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:car_rental.db";
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Driver not found at runtime - project includes JDBC-ready code; add driver to classpath when running.
            System.err.println("SQLite JDBC driver not found. Add it to project classpath if you want DB connectivity.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}

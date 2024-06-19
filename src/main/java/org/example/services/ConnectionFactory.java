package org.example.services;

import java.sql.*;

public class ConnectionFactory{
    private static final String URL = "jdbc:mysql://localhost:3306/dbfabrica";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(Connection connection)  {
        try {
            if(connection != null) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection connection, PreparedStatement statement)  {
        close(connection);
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet rs)  {
        close(connection, statement);
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

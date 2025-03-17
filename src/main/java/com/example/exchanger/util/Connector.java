package com.example.exchanger.util;



import java.sql.*;


public class Connector {
    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASSWORD = "0000";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgresSQL Driver not found", e);
        }
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void closeConnection(Connection connection, Statement statement, ResultSet resultSet) throws Exception {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            throw new Exception();
        }

    }
}

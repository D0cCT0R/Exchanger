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
            throw new RuntimeException("PostgresSQL драйвер не найден", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void closeConnection(Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new SQLException();
        }

    }
}

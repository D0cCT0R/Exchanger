package com.example.exchanger.util;



import java.sql.*;
import java.util.Properties;


public class Connector {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public Connector() {
        Properties properties = new Properties();
        this.URL = properties.getProperty("db_url");
        this.USER = properties.getProperty("db_user");
        this.PASSWORD = properties.getProperty("db_password");
    }

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

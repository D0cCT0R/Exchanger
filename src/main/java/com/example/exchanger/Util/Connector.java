package com.example.exchanger.util;



import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Properties;


public class Connector {
    private final HikariDataSource DATA_SOURCE;

    public Connector() {
        HikariConfig config = new HikariConfig();
        Properties properties = new Properties();
        config.setJdbcUrl(properties.getProperty("db_url"));
        config.setUsername(properties.getProperty("db_user"));
        config.setPassword(properties.getProperty("db_password"));
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(60000);
        DATA_SOURCE = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public void closeResources(AutoCloseable... resources) {
        for (AutoCloseable res:resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    throw new RuntimeException("Неудалось закрыть ресурсы " + e);
                }
            }
        }
    }


}

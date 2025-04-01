package com.example.exchanger.util;



import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


/**
 * Утилита для работы с пулом соединений HikariCP
 */

public class Connector {
    private final HikariDataSource dataSource;
    public Connector() {
        try {
            HikariConfig config = new HikariConfig();
            Properties properties = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
            properties.load(input);
            config.setJdbcUrl(properties.getProperty("db_url"));
            config.setUsername(properties.getProperty("db_user"));
            config.setPassword(properties.getProperty("db_password"));
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(60000);
            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("Неудалось загрузить конфиг", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации пула соединений", e);
        }
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

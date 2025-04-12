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
    private static final HikariDataSource dataSource;
    static {
            try {
                HikariConfig config = new HikariConfig();
                Properties properties = new Properties();
                InputStream input = Connector.class.getClassLoader().getResourceAsStream("application.properties");
                properties.load(input);
                config.setJdbcUrl(properties.getProperty("db.url"));
                config.setUsername(properties.getProperty("db.user"));
                config.setPassword(properties.getProperty("db.password"));
                config.setDriverClassName(properties.getProperty("db.driver"));
                config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("hikari.maximum.pool.size")));
                config.setMinimumIdle(Integer.parseInt(properties.getProperty("hikari.minimum.idle")));
                config.setConnectionTimeout(Integer.parseInt(properties.getProperty("hikari.connection.timeout")));
                config.setIdleTimeout(Integer.parseInt(properties.getProperty("hikari.idle.timeout")));
                dataSource = new HikariDataSource(config);
            } catch (IOException e) {
                throw new RuntimeException("Неудалось загрузить конфиг", e);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка инициализации пула соединений", e);
            }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

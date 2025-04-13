package com.example.exchanger.dao;

import com.example.exchanger.Exception.CurrencyAlreadyExistsException;
import com.example.exchanger.Exception.DatabaseIsNotAvailableException;
import com.example.exchanger.Exception.FailedToRetrieveIdException;
import com.example.exchanger.mapper.DataMapper;
import com.example.exchanger.model.Currency;
import com.example.exchanger.util.Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {
    private static final String SELECT_ALL_SQL = "select * from currencies";
    private static final String SELECT_ONE_SQL = "select * from currencies where code=?";
    private static final String SAVE_CUR_SQL = "insert into currencies (full_name, code, sign) values (?, ?, ?)";

    public List<Currency> getAll() {
        List<Currency> list = new ArrayList<>();
        try (Connection connection = Connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                list.add(DataMapper.mapToCurrency(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailableException("База данных недоступна");
        }
    }

    public Currency getOne(String code) {
        try (Connection connection = Connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_SQL)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return DataMapper.mapToCurrency(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailableException("База данных недоступна");
        }
    }

    public Currency save(String name, String code, String sign) {
        try (Connection connection = Connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CUR_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, code);
            preparedStatement.setString(3, sign);
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return new Currency(resultSet.getInt(1), name, code, sign);
                }
                throw new FailedToRetrieveIdException("Не удалось получить ID созданного курса");
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new CurrencyAlreadyExistsException("Валюта с таким кодом уже существует");
            }
            throw new DatabaseIsNotAvailableException("База данных недоступна");
        }
    }
}


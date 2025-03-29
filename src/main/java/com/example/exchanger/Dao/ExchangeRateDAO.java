package com.example.exchanger.dao;



import com.example.exchanger.Exception.ApiException;
import com.example.exchanger.Exception.CurrencyAlreadyExists;
import com.example.exchanger.Exception.CurrencyNotFound;
import com.example.exchanger.Exception.DatabaseIsNotAvailable;
import com.example.exchanger.model.Currency;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//TODO РАЗОБРАТЬСЯ В ПОДРОБНОСТЯХ ЧТО ТАКОЕ ПУЛ СОЕДИНЕНИЙ И ДОБАВИТЬ В ПРИЛОЖЕНИЕ HIKARICP
//TODO УБРАТЬ СОЗДАНИЕ CONNECTION STATEMENT RESULTSET В ОТДЕЛЬНЫЙ МЕТОД(МОЖЕТ РЕШИТЬСЯ ПУЛОМ СОЕДИНЕНИЙ)
//TODO ПОРАБОТАТЬ НАД ОБРАБОТКОЙ ОШИБОК

public class ExchangeRateDAO {
    Connector connector;

    public ExchangeRateDAO(Connector connector) {
        this.connector = connector;
    }

    public List<ExchangeRate> getAll() {
        List<ExchangeRate> list = new ArrayList<>();
        try {
            Connection connection = connector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("select er.id as exchange_rate_id, er.rate, base_curr.id as base_id, base_curr.code as base_code, " +
                            "base_curr.full_name as base_name, base_curr.sign as base_sign, target_curr.id as target_id, target_curr.code as target_code, target_curr.full_name as target_name, target_curr.sign as target_sign from exchange_rates er " +
                            "join currencies base_curr on er.base_currency_id=base_curr.id join currencies target_curr on er.target_currency_id=target_curr.id");
            while (resultSet.next()) {
                list.add(mapToExchangeRate(resultSet));
            }
            connector.closeConnection(connection, statement, resultSet);
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
        return list;
    }

    public ExchangeRate getOne(String baseCurr, String targetCurr) {
        try {
            Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("select er.id as exchange_rate_id, er.rate, base_curr.id as base_id, base_curr.code as base_code, " +
                    "base_curr.full_name as base_name, base_curr.sign as base_sign, target_curr.id as target_id, target_curr.code as target_code, target_curr.full_name as target_name, target_curr.sign as target_sign from exchange_rates er " +
                    "join currencies base_curr on er.base_currency_id=base_curr.id join currencies target_curr on er.target_currency_id=target_curr.id where base_curr.code=? and target_curr.code=?");
            preparedStatement.setString(1, baseCurr);
            preparedStatement.setString(2, targetCurr);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapToExchangeRate(resultSet);
            } else {
                throw new CurrencyNotFound("Валюты не сущестует в базе данных");
            }
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
    }

    public ExchangeRate updateRate(String baseCurr, String targetCurr, float rate) {
        try{
            ExchangeRate exchangeRate = getOne(baseCurr, targetCurr);
            Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update exchange_rates set rate=? where id=?");
            preparedStatement.setFloat(1, rate);
            preparedStatement.setInt(2, exchangeRate.getId());
            preparedStatement.executeUpdate();
            exchangeRate.setRate(rate);
            return exchangeRate;
        } catch (ApiException e) {
            throw new CurrencyNotFound("Валютная пара отсутствует в базе данных");
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
    }

    public ExchangeRate saveRate(Currency baseCurrency, Currency targetCurrency, float rate) {
        try {
            String sql = "insert into exchange_rates (base_currency_id, target_currency_id, rate) values (?,?,?)";
            Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, baseCurrency.getId());
            preparedStatement.setInt(2, targetCurrency.getId());
            preparedStatement.setFloat(3, rate);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
            } else {
                int id = 0;
                return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())){
                throw new CurrencyAlreadyExists("Валютная пара с таким кодом уже существует");
            }
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
    }

    private ExchangeRate mapToExchangeRate(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getInt("base_id"),
                resultSet.getString("base_code"),
                resultSet.getString("base_name"),
                resultSet.getString("base_sign"));
        Currency targetCurrency = new Currency(
                resultSet.getInt("target_id"),
                resultSet.getString("target_code"),
                resultSet.getString("target_name"),
                resultSet.getString("target_sign"));
        return new ExchangeRate(
                resultSet.getInt("exchange_rate_id"),
                baseCurrency,
                targetCurrency,
                resultSet.getFloat("rate"));
    }
}

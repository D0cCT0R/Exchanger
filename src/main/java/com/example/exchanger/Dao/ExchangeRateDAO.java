package com.example.exchanger.dao;


import com.example.exchanger.Exception.*;
import com.example.exchanger.model.Currency;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ExchangeRateDAO {
    Connector connector;

    private static final String SELECT_ALL_SQL = "select er.id as exchange_rate_id, er.rate, base_curr.id as base_id, base_curr.code as base_code, " +
            "base_curr.full_name as base_name, base_curr.sign as base_sign, target_curr.id as target_id, target_curr.code as target_code, target_curr.full_name as target_name, target_curr.sign as target_sign from exchange_rates er " +
            "join currencies base_curr on er.base_currency_id=base_curr.id join currencies target_curr on er.target_currency_id=target_curr.id";
    private static final String SELECT_ONE_SQL = SELECT_ALL_SQL + " where base_curr.code=? and target_curr.code=?";
    private static final String UPDATE_RATE_SQL = "update exchange_rates set rate=? where id=?";
    private static final String SAVE_RATE_SQL = "insert into exchange_rates (base_currency_id, target_currency_id, rate) values (?,?,?)";

    public ExchangeRateDAO(Connector connector) {
        this.connector = connector;
    }

    public List<ExchangeRate> getAll() {
        List<ExchangeRate> list = new ArrayList<>();
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapToExchangeRate(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailableException("База данных недоступна");
        }
    }

    public ExchangeRate getOne(String baseCurr, String targetCurr) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ONE_SQL)) {
            preparedStatement.setString(1, baseCurr);
            preparedStatement.setString(2, targetCurr);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToExchangeRate(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailableException("База данных недоступна");
        }
    }

    public ExchangeRate updateRate(String baseCurr, String targetCurr, BigDecimal rate) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RATE_SQL)) {
            ExchangeRate exchangeRate = getOne(baseCurr, targetCurr);
            if (exchangeRate == null) {
                return null;
            }
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, exchangeRate.getId());
            preparedStatement.executeUpdate();
            exchangeRate.setRate(rate);
            return exchangeRate;
        } catch (SQLException e) {
            throw new DatabaseIsNotAvailableException("База данных недоступна");
        }
    }

    public ExchangeRate saveRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, baseCurrency.getId());
            preparedStatement.setInt(2, targetCurrency.getId());
            preparedStatement.setBigDecimal(3, rate);
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                if (resultSet.next()) {
                    return new ExchangeRate(resultSet.getInt(1), baseCurrency, targetCurrency, rate);
                }
                throw new FailedToRetrieveIdException("Не удалось получить ID созданного обменного курса");
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new CurrencyPairAlreadyExistsException("Курс для пары уже существует");
            }
            throw new DatabaseIsNotAvailableException("База данных недоступна");
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
                resultSet.getBigDecimal("rate"));
    }
}

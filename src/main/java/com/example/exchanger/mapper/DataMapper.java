package com.example.exchanger.mapper;

import com.example.exchanger.model.Currency;
import com.example.exchanger.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataMapper {
    public static Currency mapToCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign"));
    }

    public static ExchangeRate mapToExchangeRate(ResultSet resultSet) throws SQLException {
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

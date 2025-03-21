package com.example.exchanger.dao;


import com.example.exchanger.Exception.DatabaseIsNotAvailable;
import com.example.exchanger.model.Currency;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
                Currency baseCurrency = new Currency(
                        resultSet.getInt("base_id"), 
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign"));
                Currency targetCurrency = new Currency(
                        resultSet.getInt("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign"));
                ExchangeRate exchangeRate = new ExchangeRate(
                        resultSet.getInt("exchange_rate_id"),
                        baseCurrency,
                        targetCurrency,
                        resultSet.getFloat("rate"));
                list.add(exchangeRate);
            }
            connector.closeConnection(connection, statement, resultSet);
        } catch (Exception e) {
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
        return list;
    }
}

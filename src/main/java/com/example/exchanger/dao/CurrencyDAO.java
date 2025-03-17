package com.example.exchanger.dao;
import com.example.exchanger.Exception.CurrencyNotFound;
import com.example.exchanger.Exception.DatabaseIsNotAvailable;
import com.example.exchanger.model.Currency;
import com.example.exchanger.util.Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {
    private final Connector connector;

    public CurrencyDAO(Connector connector){
        this.connector = connector;
    }

    public List<Currency> getAll() {
        List<Currency> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connector.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from currencies");
            while(resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setFullName(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
                list.add(currency);
            }
            connector.closeConnection(connection, statement, resultSet);
        } catch (Exception e) {
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
        return list;
    }

    public Currency getOne(String code) {
        try {
            String sql = "select * from currencies where code=?";
            Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setCode(resultSet.getString("code"));
                currency.setFullName(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
                connector.closeConnection(connection, preparedStatement, resultSet);
                return currency;
            } else {
                throw new CurrencyNotFound("Валюта не найдена");
            }
        } catch (Exception e){
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
    }
}

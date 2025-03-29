package com.example.exchanger.dao;
import com.example.exchanger.Exception.CurrencyAlreadyExists;
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
                currency.setName(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
                list.add(currency);
            }
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
                currency.setName(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
                return currency;
            } else {
                throw new CurrencyNotFound("Валюта не найдена");
            }
        } catch (SQLException e){
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
    }

    public Currency save(String name, String code, String sign) {
        try {
            String sql = "insert into currencies (full_name, code, sign) values (?, ?, ?)";
            Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, code);
            preparedStatement.setString(3, sign);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                Currency currency = new Currency();
                currency.setId(id);
                currency.setName(name);
                currency.setCode(code);
                currency.setSign(sign);

                return currency;
            } else{
                Currency currency = new Currency();
                currency.setId(0);
                currency.setName(name);
                currency.setCode(code);
                currency.setSign(sign);

                return currency;
            }
        } catch (SQLException e){
            if ("23505".equals(e.getSQLState())){
                throw new CurrencyAlreadyExists("Валюта с таким кодом уже существует");
            }
            throw new DatabaseIsNotAvailable("База данных недоступна");
        }
    }
}


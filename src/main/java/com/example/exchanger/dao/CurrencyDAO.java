package com.example.exchanger.dao;
import com.example.exchanger.model.Currency;
import com.example.exchanger.util.Connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {
    private final Connector connector;

    public CurrencyDAO(Connector connector){
        this.connector = connector;
    }

    public List<Currency> getAll() throws ClassNotFoundException {
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
        } catch (ClassNotFoundException | SQLException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
        return list;
    }

}

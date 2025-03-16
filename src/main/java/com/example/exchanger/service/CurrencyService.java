package com.example.exchanger.service;

import com.example.exchanger.dao.CurrencyDAO;
import com.example.exchanger.model.Currency;
import com.example.exchanger.util.Connector;

import java.util.List;

public class CurrencyService {
    CurrencyDAO currencyDAO;

    public CurrencyService(Connector connector) {
        this.currencyDAO = new CurrencyDAO(connector);
    }

    public List<Currency> getAllCurrencies() throws ClassNotFoundException {
        return currencyDAO.getAll();
    }

    public void saveCurrency () {

    }
}

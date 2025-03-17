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

    public List<Currency> getAllCurrencies() throws Exception {
        return currencyDAO.getAll();
    }
    public Currency getOneCurrency(String code) throws Exception{
        return currencyDAO.getOne(code);
    }
    public void saveCurrency () {

    }
}

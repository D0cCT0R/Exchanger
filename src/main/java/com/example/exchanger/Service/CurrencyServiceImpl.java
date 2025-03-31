package com.example.exchanger.service;

import com.example.exchanger.dao.CurrencyDAO;
import com.example.exchanger.model.Currency;
import com.example.exchanger.util.Connector;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService{
    CurrencyDAO currencyDAO;

    public CurrencyServiceImpl(Connector connector) {
        this.currencyDAO = new CurrencyDAO(connector);
    }

    public List<Currency> getAllCurrencies() {
        return currencyDAO.getAll();
    }
    public Currency getOneCurrency(String code) {
        return currencyDAO.getOne(code);
    }
    public Currency saveCurrency (String name, String code, String sign) {
        return currencyDAO.save(name, code, sign);
    }
}

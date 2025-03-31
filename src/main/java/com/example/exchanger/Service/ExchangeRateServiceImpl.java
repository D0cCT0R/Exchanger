package com.example.exchanger.service;

import com.example.exchanger.Exception.CurrencyNotFound;
import com.example.exchanger.dao.CurrencyDAO;
import com.example.exchanger.dao.ExchangeRateDAO;
import com.example.exchanger.model.Currency;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService{
    ExchangeRateDAO exchangeRateDao;
    CurrencyDAO currencyDAO;
    public ExchangeRateServiceImpl(Connector connector) {
        this.exchangeRateDao = new ExchangeRateDAO(connector);
        this.currencyDAO = new CurrencyDAO(connector);
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.getAll();
    }

    public ExchangeRate getExchangeRate(String baseCurr, String targetCurr) {
        return exchangeRateDao.getOne(baseCurr, targetCurr);
    }
    public ExchangeRate updateExchangeRate(String baseCurr, String targetCurr, float rate) {
        return exchangeRateDao.updateRate(baseCurr, targetCurr, rate);
    }
    public ExchangeRate saveExchangeRate(String baseCurrencyCode, String targetCurrencyCode, float rate) {
        try {
            Currency baseCurrency = currencyDAO.getOne(baseCurrencyCode);
            Currency targetCurrency = currencyDAO.getOne(targetCurrencyCode);
            return exchangeRateDao.saveRate(baseCurrency, targetCurrency, rate);
        } catch (CurrencyNotFound e) {
            throw new CurrencyNotFound("Одна (или обе) валюты из валютной пары не существует в БД");
        }
    }
}

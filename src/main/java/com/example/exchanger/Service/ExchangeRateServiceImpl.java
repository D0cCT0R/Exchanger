package com.example.exchanger.service;

import com.example.exchanger.dao.CurrencyDAO;
import com.example.exchanger.dao.ExchangeRateDAO;
import com.example.exchanger.model.Currency;
import com.example.exchanger.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService{
    private final ExchangeRateDAO exchangeRateDao = new ExchangeRateDAO();;
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.getAll();
    }

    public ExchangeRate getExchangeRate(String baseCurr, String targetCurr) {
        return exchangeRateDao.getOne(baseCurr, targetCurr);
    }
    public ExchangeRate updateExchangeRate(String baseCurr, String targetCurr, BigDecimal rate) {
        return exchangeRateDao.updateRate(baseCurr, targetCurr, rate);
    }
    public ExchangeRate saveExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        Currency baseCurrency = currencyDAO.getOne(baseCurrencyCode);
        if (baseCurrency == null) {
            return null;
        }
        Currency targetCurrency = currencyDAO.getOne(targetCurrencyCode);
        if (targetCurrency == null) {
            return null;
        }
        return exchangeRateDao.saveRate(baseCurrency, targetCurrency, rate);
    }
}

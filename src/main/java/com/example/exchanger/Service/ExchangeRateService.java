package com.example.exchanger.service;

import com.example.exchanger.dao.ExchangeRateDAO;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.util.List;

public class ExchangeRateService {
    ExchangeRateDAO exchangeRateDao;

    public ExchangeRateService(Connector connector) {
        this.exchangeRateDao = new ExchangeRateDAO(connector);
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.getAll();
    }

    public ExchangeRate getExchangeRate(String baseCurr, String targetCurr) {
        return exchangeRateDao.getOne(baseCurr, targetCurr);
    }
}

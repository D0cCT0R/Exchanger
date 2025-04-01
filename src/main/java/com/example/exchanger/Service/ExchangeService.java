package com.example.exchanger.service;

import com.example.exchanger.dao.ExchangeRateDAO;
import com.example.exchanger.model.Exchange;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ExchangeService {
    ExchangeRateDAO exchangeRateDAO;
    private static final String USD_CODE = "USD";

    public ExchangeService(Connector connector) {
        this.exchangeRateDAO = new ExchangeRateDAO(connector);
    }

    public Exchange currencyExchange(String baseCurr, String targetCurr, BigDecimal amount) {
        ExchangeRate exchangeRate = exchangeRateDAO.getOne(baseCurr, targetCurr);
        if (exchangeRate != null) {
            return new Exchange(
                    exchangeRate.getBaseCurrency(),
                    exchangeRate.getTargetCurrency(),
                    exchangeRate.getRate(),
                    amount,
                    amount.multiply(exchangeRate.getRate()));
        }
        ExchangeRate reversRate = exchangeRateDAO.getOne(targetCurr, baseCurr);
        if (reversRate != null) {
            return new Exchange(
                    reversRate.getTargetCurrency(),
                    reversRate.getBaseCurrency(),
                    (BigDecimal.ONE.divide(reversRate.getRate(), 6, RoundingMode.HALF_UP)),
                    amount,
                    amount.multiply(BigDecimal.ONE.divide(reversRate.getRate(), 6, RoundingMode.HALF_UP)));
        }
        ExchangeRate usdFromRate = exchangeRateDAO.getOne(USD_CODE, baseCurr);
        ExchangeRate usdToRate = exchangeRateDAO.getOne(USD_CODE, targetCurr);
        if (usdFromRate != null && usdToRate != null) {
            return new Exchange(usdFromRate.getTargetCurrency(),
                    usdToRate.getTargetCurrency(),
                    (usdToRate.getRate().divide(usdFromRate.getRate(), 6, RoundingMode.HALF_UP)),
                    amount,
                    (amount.multiply(usdToRate.getRate().divide(usdFromRate.getRate(), 6, RoundingMode.HALF_UP))));
        }
        return null;
    }

}

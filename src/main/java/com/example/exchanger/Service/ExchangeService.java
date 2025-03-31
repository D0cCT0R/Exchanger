package com.example.exchanger.service;

import com.example.exchanger.Exception.ExchangeRateNotFoundException;
import com.example.exchanger.dao.ExchangeRateDAO;
import com.example.exchanger.model.Exchange;
import com.example.exchanger.model.ExchangeRate;
import com.example.exchanger.util.Connector;

import java.math.BigDecimal;

//TODO доделать добавить bigDecimal
//TODO сделать проверку деления на 0
//TODO понять почему жесткая связь с дао это плохо и почему лучше внедрять зависимости через конструктор

public class ExchangeService {
    ExchangeRateDAO exchangeRateDAO;
    private static final String USD_CODE = "USD";

    public ExchangeService(Connector connector) {
        this.exchangeRateDAO = new ExchangeRateDAO(connector);
    }

    public Exchange currencyExchange(String baseCurr, String targetCurr, float amount) {
        ExchangeRate exchangeRate = exchangeRateDAO.getOne(baseCurr, targetCurr);
        if (exchangeRate != null) {
            return new Exchange(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(),
                    exchangeRate.getRate(), amount, amount * exchangeRate.getRate());
        }
        ExchangeRate reversRate = exchangeRateDAO.getOne(targetCurr, baseCurr);
        if (reversRate != null) {
            return new Exchange(reversRate.getTargetCurrency(), reversRate.getBaseCurrency(),
                    (1 / reversRate.getRate()), amount, amount * (1 / reversRate.getRate()));
        }
        ExchangeRate usdFromRate = exchangeRateDAO.getOne(USD_CODE, baseCurr);
        ExchangeRate usdToRate = exchangeRateDAO.getOne(USD_CODE, targetCurr);
        if (usdFromRate != null && usdToRate != null) {
            return new Exchange(usdFromRate.getTargetCurrency(), usdToRate.getTargetCurrency(),
                    (usdToRate.getRate() / usdFromRate.getRate()), amount, (amount * (usdToRate.getRate() / usdFromRate.getRate())));
        }
        throw new ExchangeRateNotFoundException("Не удалось найти подходящий курс в базе данных");
    }

}

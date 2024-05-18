package com.example.exchangeportal.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.provider.CurrencyProvider;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyProvider currencyProvider;

    @Autowired
    private ExchangeRateService exchangeRateService;

    public void fetchAndSaveCurrenciesFromApi() throws ApiException, ParsingException {
        currencyRepository.saveAll(currencyProvider.fetchAll());
    }

    public Currency getCurrencyWithExchangeRates(Long id, LocalDate fromDate, LocalDate toDate) throws ApiException, ParsingException {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find currency with id: " + id));

        currency.setExchangeRates(exchangeRateService.getAndPopulateMissingExchangeRatesForCurrency(
                currency, fromDate, toDate));

        return currency;
    }
}

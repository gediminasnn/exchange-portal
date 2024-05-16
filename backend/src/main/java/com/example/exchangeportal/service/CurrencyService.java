package com.example.exchangeportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.service.provider.CurrencyProvider;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyProvider currencyProvider;

    public void fetchAndSaveCurrenciesFromApi() throws ApiException, ParsingException {
        currencyRepository.saveAll(currencyProvider.fetchAll());
    }
}

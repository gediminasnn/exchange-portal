package com.example.exchangeportal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.service.provider.ExchangeRateProvider;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateProvider exchangeRateProvider;

    public void fetchAndSaveExchangeRatesFromApi() throws ApiException, ParsingException {
        exchangeRateRepository.saveAll(exchangeRateProvider.fetchAllByDate(LocalDate.now()));
    }

    public List<ExchangeRate> getLatestExchangeRates() {
        return exchangeRateRepository.findAllWithLatestDate();
    }
}

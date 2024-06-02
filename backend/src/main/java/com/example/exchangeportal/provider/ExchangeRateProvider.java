package com.example.exchangeportal.provider;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exchangeportal.apiclient.LbApiClient;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.parser.ExchangeRateXmlParser;

@Component
public class ExchangeRateProvider {
    private LbApiClient lbApiClient;
    private ExchangeRateXmlParser exchangeRateXmlParser;

    @Autowired
    public ExchangeRateProvider(LbApiClient lbApiClient, ExchangeRateXmlParser exchangeRateXmlParser) {
        this.lbApiClient = lbApiClient;
        this.exchangeRateXmlParser = exchangeRateXmlParser;
    }

    public List<ExchangeRate> fetchAllByDate(LocalDate date)
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        return exchangeRateXmlParser.parseAll(lbApiClient.fetchExchangeRatesData(date.toString()));
    }

    public List<ExchangeRate> fetchAllForCurrencyByDateBetween(Currency currency, LocalDate fromDate, LocalDate toDate)
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        String data = lbApiClient.fetchExchangeRatesForCurrencyData(currency.getCode(), fromDate.toString(),
                toDate.toString());
        return exchangeRateXmlParser.parseAll(data);
    }
}

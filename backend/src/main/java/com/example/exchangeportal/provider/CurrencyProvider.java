package com.example.exchangeportal.provider;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exchangeportal.apiclient.LbApiClient;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.parser.CurrencyXmlParser;

@Component
public class CurrencyProvider {
    private LbApiClient lbApiClient;
    private CurrencyXmlParser currencyXmlParser;

    @Autowired
    public CurrencyProvider(LbApiClient lbApiClient, CurrencyXmlParser currencyXmlParser) {
        this.lbApiClient = lbApiClient;
        this.currencyXmlParser = currencyXmlParser;
    }

    public List<Currency> fetchAll()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        return currencyXmlParser.parseAll(lbApiClient.fetchCurrencyListData());
    }
}

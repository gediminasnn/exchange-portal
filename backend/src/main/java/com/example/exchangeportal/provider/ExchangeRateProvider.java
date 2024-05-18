package com.example.exchangeportal.provider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.parser.ExchangeRateXmlParser;

@Component
public class ExchangeRateProvider {
    public HttpClient httpClient;

    @Autowired
    private ExchangeRateXmlParser exchangeRateXmlParser;

    private static final String EXCHANGE_RATES_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRates";
    private static final String EXCHANGE_RATES_FOR_CURRENCY_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency";

    public ExchangeRateProvider() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public ExchangeRateProvider(HttpClient httpClient, ExchangeRateXmlParser exchangeRateXmlParser) {
        this.httpClient = httpClient;
        this.exchangeRateXmlParser = exchangeRateXmlParser;
    }

    public List<ExchangeRate> fetchAllByDate(LocalDate date)
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        String requestBody = "tp=LT&dt=" + date.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EXCHANGE_RATES_API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new BadHttpClientRequestException("Unexpected error while sending request via httpClient.", e);
        }

        if (response.statusCode() != 200) {
            throw new BadApiResponseException(
                    "Bad response: expected 200 http status.");
        }

        List<ExchangeRate> exchangeRates = exchangeRateXmlParser.parseAll(response.body());

        return exchangeRates;
    }

    public List<ExchangeRate> fetchAllForCurrencyByDateBetween(Currency currency,
            LocalDate fromDate, LocalDate toDate)
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        String requestBody = "tp=LT&ccy=" + currency.getCode() + "&dtFrom=" + fromDate.toString() + "&dtTo="
                + toDate.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EXCHANGE_RATES_FOR_CURRENCY_API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new BadHttpClientRequestException("Unexpected error while sending request via httpClient.", e);
        }
        if (response.statusCode() != 200) {
            throw new BadApiResponseException("Bad response: expected 200 http status.");
        }

        List<ExchangeRate> missingExchangeRates = exchangeRateXmlParser.parseAll(response.body());

        return missingExchangeRates;
    }
}

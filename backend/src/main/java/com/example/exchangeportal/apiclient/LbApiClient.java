package com.example.exchangeportal.apiclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;

import com.example.exchangeportal.exception.BadHttpClientRequestException;

@Component
public class LbApiClient {
    private static final String GET_CURRENCY_LIST_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrencyList?";
    private static final String GET_EXCHANGE_RATES_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRates";
    private static final String GET_EXCHANGE_RATES_FOR_CURRENCY_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency";

    private HttpClient httpClient;

    public LbApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public LbApiClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String fetchCurrencyListData() throws BadHttpClientRequestException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_CURRENCY_LIST_API_URL))
                .header("Accept", "application/xml")
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new BadHttpClientRequestException("Unexpected error while sending request via httpClient.", e);
        }

        if (response.statusCode() != 200) {
            throw new BadHttpClientRequestException("Bad response: expected 200 http status.");
        }

        return response.body();
    }

    public String fetchExchangeRatesData(String date) throws BadHttpClientRequestException {
        String requestBody = "tp=LT&dt=" + date;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_EXCHANGE_RATES_API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new BadHttpClientRequestException("Unexpected error while sending request via httpClient.", e);
        }

        if (response.statusCode() != 200) {
            throw new BadHttpClientRequestException("Bad response: expected 200 http status.");
        }

        return response.body();
    }

    public String fetchExchangeRatesForCurrencyData(String currencyCode, String fromDate, String toDate)
            throws BadHttpClientRequestException {
        String requestBody = "tp=LT&ccy=" + currencyCode + "&dtFrom=" + fromDate + "&dtTo=" + toDate;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_EXCHANGE_RATES_FOR_CURRENCY_API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new BadHttpClientRequestException("Unexpected error while sending request via httpClient.", e);
        }

        if (response.statusCode() != 200) {
            throw new BadHttpClientRequestException("Bad response: expected 200 http status.");
        }

        return response.body();
    }
}

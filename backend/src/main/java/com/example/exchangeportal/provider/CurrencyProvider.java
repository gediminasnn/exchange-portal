package com.example.exchangeportal.provider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.parser.CurrencyXmlParser;

@Component
public class CurrencyProvider {
    private HttpClient httpClient;

    private static final String CURRENCY_LIST_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrencyList?";

    @Autowired
    private CurrencyXmlParser currencyXmlParser;

    public CurrencyProvider() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public CurrencyProvider(HttpClient httpClient, CurrencyXmlParser currencyXmlParser) {
        this.httpClient = httpClient;
        this.currencyXmlParser = currencyXmlParser;
    }

    public List<Currency> fetchAll()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CURRENCY_LIST_API_URL))
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
            throw new BadApiResponseException(
                    "Bad response: expected 200 http status.");
        }

        List<Currency> currencies = currencyXmlParser.parseAll(response.body());

        return currencies;
    }
}

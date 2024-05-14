package com.example.exchangeportal.service.provider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

@Component
public class CurrencyXmlProvider {
    private HttpClient httpClient;

    private static final String CURRENCY_LIST_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrencyList?";

    public CurrencyXmlProvider() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public CurrencyXmlProvider(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String fetchCurrencyListXml() throws IOException, InterruptedException {
        HttpRequest request = prepareHttpRequest();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 || response.body().isEmpty()) {
            throw new IOException("Unexpected response from api");
        }

        return response.body();
    }

    private HttpRequest prepareHttpRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(CURRENCY_LIST_API_URL))
                .header("Accept", "application/xml")
                .GET()
                .build();
    }
}

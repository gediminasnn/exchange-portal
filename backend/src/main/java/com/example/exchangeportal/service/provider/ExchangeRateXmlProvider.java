package com.example.exchangeportal.service.provider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

@Component
public class ExchangeRateXmlProvider {
	public HttpClient httpClient;

	private static final String GET_CURRENT_FX_RATES_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU";

	public ExchangeRateXmlProvider() {
		this.httpClient = HttpClient.newHttpClient();
	}

	public ExchangeRateXmlProvider(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public String fetchXmlDataFromApi() throws IOException, InterruptedException {
		HttpRequest request = prepareHttpRequest();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200 || response.body().isEmpty()) {
			throw new IOException("Unexpected response from api");
		}

		return response.body();
	}

	private HttpRequest prepareHttpRequest() {
		return HttpRequest.newBuilder()
				.uri(URI.create(GET_CURRENT_FX_RATES_API_URL))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.GET()
				.build();
	}
}

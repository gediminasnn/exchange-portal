package com.example.exchangeportal.service.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CurrencyProviderXmlTests {
	@Mock
	private HttpClient mockHttpClient;

	@Mock
	private HttpResponse<String> mockHttpResponse;

	private CurrencyXmlProvider currencyXmlProvider;

	@BeforeEach
	public void setup() {
		currencyXmlProvider = new CurrencyXmlProvider(mockHttpClient);
	}

	@Test
	public void testFetchXmlDataFromApi_Success() throws Exception {
		when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
				.thenReturn(mockHttpResponse);
		when(mockHttpResponse.statusCode()).thenReturn(200);
		when(mockHttpResponse.body()).thenReturn("<sampleXmlResponse>some data</sampleXmlResponse>");

		String result = currencyXmlProvider.fetchCurrencyListXml();
		assertEquals("<sampleXmlResponse>some data</sampleXmlResponse>", result);
	}

	@Test
	public void testFetchXmlDataFromApi_EmptyResponseBody() throws Exception {
		when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
				.thenReturn(mockHttpResponse);
		when(mockHttpResponse.statusCode()).thenReturn(200);
		when(mockHttpResponse.body()).thenReturn("");

		assertThrows(IOException.class, () -> {
			currencyXmlProvider.fetchCurrencyListXml();
		});
	}

	@Test
	public void testFetchXmlDataFromApi_Non200Response() throws Exception {
		when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
				.thenReturn(mockHttpResponse);
		when(mockHttpResponse.statusCode()).thenReturn(500);

		assertThrows(IOException.class, () -> {
			currencyXmlProvider.fetchCurrencyListXml();
		});
	}
}

package com.example.exchangeportal.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.parser.CurrencyXmlParser;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CurrencyProviderTest {
	@Mock
	private HttpClient mockHttpClient;

	@Mock
	private HttpResponse<String> mockHttpResponse;

	private CurrencyProvider currencyProvider;

	private CurrencyXmlParser currencyXmlParser;

	@BeforeEach
	void setup() {
		currencyXmlParser = new CurrencyXmlParser();
		currencyProvider = new CurrencyProvider(mockHttpClient, currencyXmlParser);
	}

	@Test
	public void testFetchAll_Success() throws Exception {
		String validXml = """
				<CcyTbl xmlns="http://www.lb.lt/WebServices/FxRates">
				    <CcyNtry>
				        <Ccy>AED</Ccy>
				        <CcyNm lang="LT">Jungtinių Arabų Emiratų dirhamas</CcyNm>
				        <CcyNm lang="EN">UAE dirham</CcyNm>
				        <CcyNbr>784</CcyNbr>
				        <CcyMnrUnts>2</CcyMnrUnts>
				    </CcyNtry>
				</CcyTbl>
				""";

		when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
				.thenReturn(mockHttpResponse);
		when(mockHttpResponse.statusCode()).thenReturn(200);
		when(mockHttpResponse.body()).thenReturn(validXml);

		List<Currency> expectedCurrencies = Arrays.asList(Currency.builder()
				.code("AED")
				.name("UAE dirham")
				.minorUnits(2)
				.build());
		List<Currency> actualCurrencies = currencyProvider.fetchAll();
		assertEquals(expectedCurrencies, actualCurrencies);
	}

	@Test
	public void testFetchAll_Non200Response() throws Exception {
		when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
				.thenReturn(mockHttpResponse);
		when(mockHttpResponse.statusCode()).thenReturn(500);

		assertThrows(BadApiResponseException.class, () -> {
			currencyProvider.fetchAll();
		});
	}
}

package com.example.exchangeportal.service;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.service.parser.ExchangeRateXmlParser;
import com.example.exchangeportal.service.provider.ExchangeRateXmlProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

	@Mock
	private ExchangeRateRepository exchangeRateRepository;

	@Mock
	private ExchangeRateXmlProvider exchangeRateProvider;

	@Mock
	private ExchangeRateXmlParser exchangeRateXmlParser;

	@InjectMocks
	private ExchangeRateService exchangeRateService;

	@Test
	public void testFetchAndSaveExchangeRatesFromApi_Success()
			throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		String xmlData = "<sampleXml>Some XML data</sampleXml>";
		LocalDate date = LocalDate.parse("2024-05-12");

		Currency usd = new Currency(null, "USD", "US Dollar", 2);
		Currency eur = new Currency(null, "EUR", "Euro", 2);

		List<ExchangeRate> rates = Arrays.asList(
				new ExchangeRate(null, usd, 1.23, date),
				new ExchangeRate(null, eur, 0.85, date));

		when(exchangeRateProvider.fetchXmlDataFromApi()).thenReturn(xmlData);
		when(exchangeRateXmlParser.parse(xmlData)).thenReturn(rates);

		exchangeRateService.fetchAndSaveExchangeRatesFromApi();

		verify(exchangeRateProvider).fetchXmlDataFromApi();
        verify(exchangeRateXmlParser).parse(xmlData);
		verify(exchangeRateRepository).saveAll(rates);
	}

	@Test
	void fetchAndSaveExchangeRatesFromApi_ExceptionThrown()
			throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		when(exchangeRateProvider.fetchXmlDataFromApi()).thenThrow(new IOException());

		assertThrows(IOException.class, () -> {
			exchangeRateService.fetchAndSaveExchangeRatesFromApi();
		});

		verify(exchangeRateProvider).fetchXmlDataFromApi();
		verifyNoInteractions(exchangeRateXmlParser);
		verifyNoInteractions(exchangeRateRepository);
	}
}

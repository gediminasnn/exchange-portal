package com.example.exchangeportal.service;

import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.service.parser.ExchangeRateXmlParser;
import com.example.exchangeportal.service.provider.LatestExchangeRateXmlProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

	@Mock
	private ExchangeRateRepository exchangeRateRepository;

	@Mock
	private LatestExchangeRateXmlProvider exchangeRateProvider;

	@Mock
	private ExchangeRateXmlParser exchangeRateParser;

	@InjectMocks
	private ExchangeRateService exchangeRateService;

	@Test
	public void testFetchAndSaveExchangeRatesFromApi()
			throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		String xmlData = "<sampleXml>Some XML data</sampleXml>";
		List<ExchangeRate> rates = Arrays.asList(
				new ExchangeRate(null, "USD", 1.23, Date.valueOf("2024-05-12")),
				new ExchangeRate(null, "EUR", 0.85, Date.valueOf("2024-05-12")));

		when(exchangeRateProvider.fetchXmlDataFromApi()).thenReturn(xmlData);
		when(exchangeRateParser.parse(xmlData)).thenReturn(rates);

		exchangeRateService.fetchAndSaveExchangeRatesFromApi();

		verify(exchangeRateRepository).saveAll(rates);
	}
}

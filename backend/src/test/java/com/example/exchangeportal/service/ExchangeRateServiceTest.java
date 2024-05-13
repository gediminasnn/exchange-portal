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
import java.time.LocalDate;
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
		LocalDate date = LocalDate.parse("2024-05-12");
		
		List<ExchangeRate> rates = Arrays.asList(
				new ExchangeRate(null, "USD", 1.23, date),
				new ExchangeRate(null, "EUR", 0.85, date));

		when(exchangeRateProvider.fetchXmlDataFromApi()).thenReturn(xmlData);
		when(exchangeRateParser.parse(xmlData)).thenReturn(rates);

		exchangeRateService.fetchAndSaveExchangeRatesFromApi();

		verify(exchangeRateRepository).saveAll(rates);
	}
}

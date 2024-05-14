package com.example.exchangeportal.service;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.service.parser.CurrencyXmlParser;
import com.example.exchangeportal.service.provider.CurrencyXmlProvider;

@Service
public class CurrencyService {
	@Autowired
	private CurrencyRepository exchangeRateRepository;

	@Autowired
	private CurrencyXmlProvider currencyProvider;

	@Autowired
	private CurrencyXmlParser currencyXmlParser;

	public void fetchAndSaveCurrenciesFromApi()
			throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		String xml = currencyProvider.fetchCurrencyListXml();
		List<Currency> exchangeRates = currencyXmlParser.parse(xml);
		exchangeRateRepository.saveAll(exchangeRates);
	}
}

package com.example.exchangeportal.service;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.service.parser.ExchangeRateXmlParser;
import com.example.exchangeportal.service.provider.ExchangeRateXmlProvider;

@Service
public class ExchangeRateService {
	@Autowired
	private ExchangeRateRepository exchangeRateRepository;

	@Autowired
	private ExchangeRateXmlProvider exchangeRateProvider;

	@Autowired
	private ExchangeRateXmlParser exchangeRateXmlParser;

	public void fetchAndSaveExchangeRatesFromApi()
			throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		String xmlData = exchangeRateProvider.fetchXmlDataFromApi();
		List<ExchangeRate> exchangeRates = exchangeRateXmlParser.parse(xmlData);
		exchangeRateRepository.saveAll(exchangeRates);
	}
}

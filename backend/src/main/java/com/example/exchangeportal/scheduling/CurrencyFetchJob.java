package com.example.exchangeportal.scheduling;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.example.exchangeportal.service.CurrencyService;

@Component
public class CurrencyFetchJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyFetchJob.class);

	@Autowired
	private CurrencyService currencyService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.info("Currencies fetching started successfully");
			currencyService.fetchAndSaveCurrenciesFromApi();
			logger.info("Currencies fetched finished successfully");
		} catch (IOException | InterruptedException | SAXException | ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		}
	}
}

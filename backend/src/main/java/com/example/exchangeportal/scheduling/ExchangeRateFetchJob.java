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

import com.example.exchangeportal.service.ExchangeRateService;

@Component
public class ExchangeRateFetchJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeRateFetchJob.class);

	@Autowired
	private ExchangeRateService exchangeRateService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			exchangeRateService.fetchAndSaveExchangeRatesFromApi();
			logger.info("Exchange rates fetched successfully");
		} catch (IOException | InterruptedException | SAXException | ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		}
	}
}

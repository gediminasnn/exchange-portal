package com.example.exchangeportal.controller;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExchangeRateService exchangeRateService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetLatestExchangeRates_ReturnsOnlyLatest() throws Exception {
		LocalDate today = LocalDate.now();

		Currency usd = Currency.builder().id(1L).code("USD").name("United States Dollar").minorUnits(2).build();
		Currency gbp = Currency.builder().id(2L).code("GBP").name("British Pound").minorUnits(2).build();

		List<ExchangeRate> expectedExchangeRates = Arrays.asList(
				ExchangeRate.builder().id(1L).currency(usd).rate(1.1).date(today).build(),
				ExchangeRate.builder().id(2L).currency(gbp).rate(0.85).date(today).build());

		when(exchangeRateService.getLatestExchangeRates()).thenReturn(expectedExchangeRates);

		MvcResult result = mockMvc.perform(get("/api/exchange-rates")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		List<ExchangeRate> actualExchangeRates = Arrays
				.asList(objectMapper.readValue(jsonResponse, ExchangeRate[].class));

		assertEquals(expectedExchangeRates, actualExchangeRates);
	}

	@Test
	public void testGetLatestExchangeRates_NoDataFound() throws Exception {
		List<ExchangeRate> expectedExchangeRates = new ArrayList<>();

		when(exchangeRateService.getLatestExchangeRates()).thenReturn(expectedExchangeRates);

		MvcResult result = mockMvc.perform(get("/api/exchange-rates")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		List<ExchangeRate> actualExchangeRates = Arrays
				.asList(objectMapper.readValue(jsonResponse, ExchangeRate[].class));

		assertEquals(expectedExchangeRates, actualExchangeRates);
	}
}

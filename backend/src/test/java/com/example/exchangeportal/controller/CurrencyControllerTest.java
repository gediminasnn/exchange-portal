package com.example.exchangeportal.controller;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.service.CurrencyService;
import com.example.exchangeportal.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testIndex_Success() throws Exception {
        Currency expectedCurrency = Currency.builder()
                .id(1L)
                .code("USD")
                .name("US Dollar")
                .minorUnits(2)
                .build();

        List<ExchangeRate> exchangeRates = Arrays.asList(
                ExchangeRate.builder().id(1L).rate(1.23).date(LocalDate.of(2024, 5, 1)).build(),
                ExchangeRate.builder().id(2L).rate(1.24).date(LocalDate.of(2024, 5, 2)).build(),
                ExchangeRate.builder().id(3L).rate(1.25).date(LocalDate.of(2024, 5, 3)).build(),
                ExchangeRate.builder().id(4L).rate(1.14).date(LocalDate.of(2024, 5, 4)).build(),
                ExchangeRate.builder().id(5L).rate(1.23).date(LocalDate.of(2024, 5, 5)).build());

        expectedCurrency.setExchangeRates(exchangeRates);

        when(currencyService.getCurrencyWithExchangeRates(
                expectedCurrency.getId(),
                LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 5, 5)))
                .thenReturn(expectedCurrency);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/currencies/1/exchange-rates")
                .param("fromDate", "2024-05-01")
                .param("toDate", "2024-05-05")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Currency actualCurrency = objectMapper.readValue(result.getResponse().getContentAsString(), Currency.class);

        assertEquals(expectedCurrency, actualCurrency);
    }
}

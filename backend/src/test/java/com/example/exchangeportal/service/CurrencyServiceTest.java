package com.example.exchangeportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.provider.CurrencyProvider;
import com.example.exchangeportal.repository.CurrencyRepository;

@SpringBootTest
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository mockCurrencyRepository;

    @Mock
    private CurrencyProvider mockCurrencyProvider;

    @Mock
    private ExchangeRateService mockExchangeRateService;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void fetchAndSaveCurrenciesFromApi_Success()
            throws ApiException, ParsingException {
        List<Currency> currencies = Arrays.asList(Currency.builder()
                .id(1L)
                .code("USD")
                .name("US Dollar")
                .minorUnits(2)
                .build());

        when(mockCurrencyProvider.fetchAll()).thenReturn(currencies);

        currencyService.fetchAndSaveCurrenciesFromApi();

        verify(mockCurrencyProvider).fetchAll();
        verify(mockCurrencyRepository).saveAll(currencies);
    }

    @Test
    public void testGetCurrencyWithExchangeRates_Success() throws Exception {
        Currency expectedCurrency = Currency.builder()
                .id(1L)
                .code("USD")
                .name("US Dollar")
                .minorUnits(2)
                .build();

        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 5);

        when(mockCurrencyRepository.findById(1L)).thenReturn(Optional.of(expectedCurrency));

        List<ExchangeRate> exchangeRates = Arrays.asList(
                ExchangeRate.builder().id(1L).rate(1.23).date(fromDate).build(),
                ExchangeRate.builder().id(2L).rate(1.24).date(LocalDate.of(2024, 5, 2)).build(),
                ExchangeRate.builder().id(3L).rate(1.25).date(LocalDate.of(2024, 5, 3)).build(),
                ExchangeRate.builder().id(4L).rate(1.14).date(LocalDate.of(2024, 5, 4)).build(),
                ExchangeRate.builder().id(5L).rate(1.23).date(toDate).build());

        when(mockExchangeRateService.getAndPopulateMissingExchangeRatesForCurrency(
                expectedCurrency,
                fromDate,
                toDate)).thenReturn(exchangeRates);

        Currency actualCurrency = currencyService.getCurrencyWithExchangeRates(
                expectedCurrency.getId(),
                fromDate,
                toDate);

        assertEquals(expectedCurrency, actualCurrency);
    }
}

package com.example.exchangeportal.service;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.parser.ExchangeRateXmlParser;
import com.example.exchangeportal.provider.ExchangeRateProvider;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.util.DateUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository mockExchangeRateRepository;

    @Mock
    private ExchangeRateProvider mockExchangeRateProvider;

    @Mock
    private ExchangeRateXmlParser mockExchangeRateXmlParser;

    @Mock
    private DateUtils mockDateUtils;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    public void testFetchAndSaveExchangeRatesFromApi_Success()
            throws ApiException, ParsingException {
        LocalDate date = LocalDate.parse("2024-05-12");

        Currency usd = Currency.builder()
                .code("USD")
                .name("US Dollar")
                .minorUnits(2)
                .build();
        Currency eur = Currency.builder()
                .code("EUR")
                .name("Euro")
                .minorUnits(2)
                .build();

        List<ExchangeRate> rates = Arrays.asList(
                ExchangeRate.builder().currency(usd).rate(1.23).date(date).build(),
                ExchangeRate.builder().currency(eur).rate(0.85).date(date).build());

        when(mockExchangeRateProvider.fetchAllByDate(LocalDate.now())).thenReturn(rates);

        exchangeRateService.fetchAndSaveExchangeRatesFromApi();

        verify(mockExchangeRateProvider).fetchAllByDate(LocalDate.now());
        verify(mockExchangeRateRepository).saveAll(rates);
    }

    @Test
    void testGetAndPopulateMissingExchangeRatesForCurrency_NoMissingRates()
            throws ApiException, ParsingException {
        Currency currency = Currency.builder().code("USD").name("US Dollar").minorUnits(2).build();
        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 3);
        List<LocalDate> missingDates = new ArrayList<>();

        List<ExchangeRate> existingRates = new ArrayList<>(Arrays.asList(
                ExchangeRate.builder().currency(currency).rate(1.23).date(LocalDate.of(2024, 5, 1)).build(),
                ExchangeRate.builder().currency(currency).rate(1.24).date(LocalDate.of(2024, 5, 2)).build(),
                ExchangeRate.builder().currency(currency).rate(1.25).date(LocalDate.of(2024, 5, 3)).build()));

        when(mockExchangeRateRepository.findAllByCurrencyAndDateBetween(currency, fromDate, toDate))
                .thenReturn(existingRates);
        when(mockDateUtils.findMissingDates(existingRates, fromDate, toDate)).thenReturn(missingDates);

        List<ExchangeRate> expectedExchangeRates = existingRates;
        expectedExchangeRates.addAll(existingRates);
        expectedExchangeRates.sort((rate1, rate2) -> rate2.getDate().compareTo(rate1.getDate()));

        List<ExchangeRate> actualExchangeRates = exchangeRateService
                .getAndPopulateMissingExchangeRatesForCurrency(currency, fromDate, toDate);

        assertEquals(expectedExchangeRates, actualExchangeRates);
        verify(mockExchangeRateRepository).findAllByCurrencyAndDateBetween(currency, fromDate, toDate);
        verifyNoMoreInteractions(mockExchangeRateProvider);
        verifyNoMoreInteractions(mockExchangeRateRepository);
    }

    @Test
    void testGetAndPopulateMissingExchangeRatesForCurrency_WithMissingRates()
            throws ApiException, ParsingException {
        Currency currency = Currency.builder().code("USD").name("US Dollar").minorUnits(2).build();
        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 3);
        List<LocalDate> missingDates = List.of(LocalDate.of(2024, 5, 2), toDate);

        List<ExchangeRate> existingRates = new ArrayList<>(Arrays.asList(
                ExchangeRate.builder().currency(currency).rate(1.23).date(LocalDate.of(2024, 5, 1)).build()));

        List<ExchangeRate> missingRates = new ArrayList<>(Arrays.asList(
                ExchangeRate.builder().currency(currency).rate(1.24).date(LocalDate.of(2024, 5, 2)).build(),
                ExchangeRate.builder().currency(currency).rate(1.25).date(LocalDate.of(2024, 5, 3)).build()));

        List<ExchangeRate> allExchangeRates = new ArrayList<>(Arrays.asList(
                ExchangeRate.builder().currency(currency).rate(1.23).date(LocalDate.of(2024, 5, 1)).build(),
                ExchangeRate.builder().currency(currency).rate(1.24).date(LocalDate.of(2024, 5, 2)).build(),
                ExchangeRate.builder().currency(currency).rate(1.25).date(LocalDate.of(2024, 5, 3)).build()));

        when(mockExchangeRateRepository.findAllByCurrencyAndDateBetween(currency, fromDate, toDate))
                .thenReturn(existingRates);
        when(mockDateUtils.findMissingDates(existingRates, fromDate, toDate)).thenReturn(missingDates);
        when(mockExchangeRateProvider.fetchAllForCurrencyByDateBetween(currency, fromDate, toDate))
                .thenReturn(allExchangeRates);

        List<ExchangeRate> expectedExchangeRates = new ArrayList<>();
        expectedExchangeRates.addAll(existingRates);
        expectedExchangeRates.addAll(missingRates);
        expectedExchangeRates.sort((rate1, rate2) -> rate2.getDate().compareTo(rate1.getDate()));

        List<ExchangeRate> actualExchangeRates = exchangeRateService
                .getAndPopulateMissingExchangeRatesForCurrency(currency, fromDate, toDate);

        assertEquals(expectedExchangeRates, actualExchangeRates);
        verify(mockExchangeRateRepository).findAllByCurrencyAndDateBetween(currency, fromDate, toDate);
        verify(mockExchangeRateProvider).fetchAllForCurrencyByDateBetween(currency, fromDate, toDate);
        verify(mockExchangeRateRepository).saveAll(missingRates);
    }
}

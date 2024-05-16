package com.example.exchangeportal.service;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.service.parser.ExchangeRateXmlParser;
import com.example.exchangeportal.service.provider.ExchangeRateProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository mockExchangeRateRepository;

    @Mock
    private ExchangeRateProvider mockExchangeRateProvider;

    @Mock
    private ExchangeRateXmlParser mockExchangeRateXmlParser;

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
    void fetchAndSaveExchangeRatesFromApi_BadHttpClientRequestExceptionThrown()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        when(mockExchangeRateProvider.fetchAllByDate(LocalDate.now())).thenThrow(new BadHttpClientRequestException());

        assertThrows(BadHttpClientRequestException.class, () -> {
            exchangeRateService.fetchAndSaveExchangeRatesFromApi();
        });

        verify(mockExchangeRateProvider).fetchAllByDate(LocalDate.now());
        verifyNoInteractions(mockExchangeRateRepository);
    }

    @Test
    void fetchAndSaveExchangeRatesFromApi_BadApiResponseExceptionThrown()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        when(mockExchangeRateProvider.fetchAllByDate(LocalDate.now())).thenThrow(new BadApiResponseException());

        assertThrows(BadApiResponseException.class, () -> {
            exchangeRateService.fetchAndSaveExchangeRatesFromApi();
        });

        verify(mockExchangeRateProvider).fetchAllByDate(LocalDate.now());
        verifyNoInteractions(mockExchangeRateRepository);
    }

    @Test
    void fetchAndSaveExchangeRatesFromApi_FailedParsingExceptionThrown()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        when(mockExchangeRateProvider.fetchAllByDate(LocalDate.now())).thenThrow(new FailedParsingException());

        assertThrows(FailedParsingException.class, () -> {
            exchangeRateService.fetchAndSaveExchangeRatesFromApi();
        });

        verify(mockExchangeRateProvider).fetchAllByDate(LocalDate.now());
        verifyNoInteractions(mockExchangeRateRepository);
    }
}

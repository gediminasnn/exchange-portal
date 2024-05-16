package com.example.exchangeportal.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.service.parser.CurrencyXmlParser;
import com.example.exchangeportal.service.provider.CurrencyProvider;

@SpringBootTest
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository mockCurrencyRepository;

    @Mock
    private CurrencyProvider mockCurrencyProvider;

    @Mock
    private CurrencyXmlParser mockCurrencyXmlParser;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    void fetchAndSaveCurrenciesFromApi_BadHttpClientRequestExceptionThrown()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        when(mockCurrencyProvider.fetchAll()).thenThrow(new BadHttpClientRequestException());

        assertThrows(BadHttpClientRequestException.class, () -> {
            currencyService.fetchAndSaveCurrenciesFromApi();
        });

        verify(mockCurrencyProvider).fetchAll();
        verifyNoInteractions(mockCurrencyRepository);
    }

    @Test
    void fetchAndSaveCurrenciesFromApi_BadApiResponseExceptionThrown()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        when(mockCurrencyProvider.fetchAll()).thenThrow(new BadApiResponseException());

        assertThrows(BadApiResponseException.class, () -> {
            currencyService.fetchAndSaveCurrenciesFromApi();
        });

        verify(mockCurrencyProvider).fetchAll();
        verifyNoInteractions(mockCurrencyRepository);
    }

    @Test
    void fetchAndSaveCurrenciesFromApi_FailedParsingExceptionThrown()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        when(mockCurrencyProvider.fetchAll()).thenThrow(new FailedParsingException());

        assertThrows(FailedParsingException.class, () -> {
            currencyService.fetchAndSaveCurrenciesFromApi();
        });

        verify(mockCurrencyProvider).fetchAll();
        verifyNoInteractions(mockCurrencyRepository);
    }
}

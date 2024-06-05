package com.example.exchangeportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.exchangeportal.dto.CurrencyDto;
import com.example.exchangeportal.dto.ExchangeRateDto;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.provider.CurrencyProvider;
import com.example.exchangeportal.repository.CurrencyRepository;
import org.modelmapper.ModelMapper;

@SpringBootTest
public class CurrencyServiceTest {

    @MockBean
    private CurrencyRepository mockCurrencyRepository;

    @MockBean
    private CurrencyProvider mockCurrencyProvider;

    @MockBean
    private ExchangeRateService mockExchangeRateService;

    @MockBean
    private ModelMapper mockModelMapper;

    @Autowired
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        Mockito.reset(mockCurrencyRepository, mockCurrencyProvider);
    }

    @Test
    void fetchAndSaveCurrenciesFromApi_Success() throws ApiException, ParsingException {
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

        List<ExchangeRateDto> exchangeRatesDtos = Arrays.asList(
                ExchangeRateDto.builder().rate(1.23).date(fromDate).build(),
                ExchangeRateDto.builder().rate(1.24).date(LocalDate.of(2024, 5, 2)).build(),
                ExchangeRateDto.builder().rate(1.25).date(LocalDate.of(2024, 5, 3)).build(),
                ExchangeRateDto.builder().rate(1.14).date(LocalDate.of(2024, 5, 4)).build(),
                ExchangeRateDto.builder().rate(1.23).date(toDate).build());

        when(mockExchangeRateService.getAndPopulateMissingExchangeRatesForCurrency(
                expectedCurrency,
                fromDate,
                toDate)).thenReturn(exchangeRatesDtos);

        CurrencyDto expectedCurrencyDto = CurrencyDto.builder()
                .id(1L).code("USD").name("US Dollar").build();

        when(mockModelMapper.map(expectedCurrency, CurrencyDto.class)).thenReturn(expectedCurrencyDto);

        CurrencyDto actualCurrencyDto = currencyService.getCurrencyWithExchangeRates(
                1L,
                fromDate,
                toDate);

        expectedCurrencyDto.setExchangeRates(exchangeRatesDtos);

        assertEquals(expectedCurrencyDto, actualCurrencyDto);
    }
}

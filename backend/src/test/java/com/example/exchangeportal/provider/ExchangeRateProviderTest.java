package com.example.exchangeportal.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.exchangeportal.apiclient.LbApiClient;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.parser.ExchangeRateXmlParser;
import com.example.exchangeportal.repository.CurrencyRepository;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateProviderTest {
    @Mock
    private LbApiClient mockLbApiClient;

    @Mock
    private CurrencyRepository mockCurrencyRepository;

    @Mock
    private ExchangeRateXmlParser mockExchangeRateXmlParser;

    private ExchangeRateProvider exchangeRateProvider;

    private ExchangeRateXmlParser exchangeRateXmlParser;

    @BeforeEach
    void setup() {
        exchangeRateXmlParser = new ExchangeRateXmlParser(mockCurrencyRepository);
        exchangeRateProvider = new ExchangeRateProvider(mockLbApiClient, exchangeRateXmlParser);
    }

    @Test
    void testFetchAllByDate_Success()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        LocalDate date = LocalDate.of(2024, 5, 10);
        String xmlResponse = """
                <?xml version="1.0" encoding="UTF-8"?>
                <FxRates xmlns:xsi="http://www.w3.org/2021/XMLSchema-instance" xsi:noNamespaceSchemaLocation="FxRatesSchema.xsd">
                    <FxRate>
                        <Tp>LT</Tp>
                        <Dt>2024-05-10</Dt>
                        <CcyAmt>
                            <Ccy>EUR</Ccy>
                            <Amt>1.0000</Amt>
                        </CcyAmt>
                        <CcyAmt>
                            <Ccy>USD</Ccy>
                            <Amt>1.0926</Amt>
                        </CcyAmt>
                    </FxRate>
                    <FxRate>
                        <Tp>LT</Tp>
                        <Dt>2024-05-10</Dt>
                        <CcyAmt>
                            <Ccy>EUR</Ccy>
                            <Amt>1.0000</Amt>
                        </CcyAmt>
                        <CcyAmt>
                            <Ccy>JPY</Ccy>
                            <Amt>142.75</Amt>
                        </CcyAmt>
                    </FxRate>
                </FxRates>
                """;

        Currency currencyUSD = Currency.builder()
                .id(1L)
                .code("USD")
                .name("United States dollar")
                .minorUnits(2)
                .build();
        Currency currencyJPY = Currency.builder()
                .id(2L)
                .code("JPY")
                .name("Japanese yen")
                .minorUnits(2)
                .build();

        ExchangeRate exchangeRateUSD = ExchangeRate.builder()
                .currency(currencyUSD)
                .rate(1.0926)
                .date(date)
                .build();
        ExchangeRate exchangeRateJPY = ExchangeRate.builder()
                .currency(currencyJPY)
                .rate(142.75)
                .date(date)
                .build();

        when(mockLbApiClient.fetchExchangeRatesData(date.toString())).thenReturn(xmlResponse);
        when(mockCurrencyRepository.findAllByCodeIn(List.of("USD", "JPY")))
                .thenReturn(List.of(currencyUSD, currencyJPY));

        List<ExchangeRate> expectedExchangeRates = List.of(exchangeRateUSD, exchangeRateJPY);
        List<ExchangeRate> actualExchangeRates = exchangeRateProvider.fetchAllByDate(date);

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }

    @Test
    void testFetchAllForCurrencyByDateBetween_Success()
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 10);
        Currency currency = Currency.builder()
                .id(1L)
                .code("USD")
                .name("United States dollar")
                .minorUnits(2)
                .build();
        String xmlResponse = """
                <?xml version="1.0" encoding="UTF-8"?>
                <FxRates xmlns:xsi="http://www.w3.org/2021/XMLSchema-instance" xsi:noNamespaceSchemaLocation="FxRatesSchema.xsd">
                    <FxRate>
                        <Tp>LT</Tp>
                        <Dt>2024-05-01</Dt>
                        <CcyAmt>
                            <Ccy>EUR</Ccy>
                            <Amt>1.0000</Amt>
                        </CcyAmt>
                        <CcyAmt>
                            <Ccy>USD</Ccy>
                            <Amt>1.0926</Amt>
                        </CcyAmt>
                    </FxRate>
                    <FxRate>
                        <Tp>LT</Tp>
                        <Dt>2024-05-02</Dt>
                        <CcyAmt>
                            <Ccy>EUR</Ccy>
                            <Amt>1.0000</Amt>
                        </CcyAmt>
                        <CcyAmt>
                            <Ccy>USD</Ccy>
                            <Amt>1.0930</Amt>
                        </CcyAmt>
                    </FxRate>
                </FxRates>
                """;

        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .currency(currency)
                .rate(1.0926)
                .date(LocalDate.of(2024, 5, 1))
                .build();
        ExchangeRate exchangeRate2 = ExchangeRate.builder()
                .currency(currency)
                .rate(1.0930)
                .date(LocalDate.of(2024, 5, 2))
                .build();

        List<ExchangeRate> expectedExchangeRates = List.of(exchangeRate1, exchangeRate2);

        when(mockLbApiClient.fetchExchangeRatesForCurrencyData(currency.getCode(), fromDate.toString(),
                toDate.toString()))
                .thenReturn(xmlResponse);
        when(mockCurrencyRepository.findAllByCodeIn(List.of("USD")))
                .thenReturn(List.of(currency));

        List<ExchangeRate> actualExchangeRates = exchangeRateProvider.fetchAllForCurrencyByDateBetween(currency,
                fromDate, toDate);

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }
}

package com.example.exchangeportal.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    @Mock
    private CurrencyRepository mockCurrencyRepository;

    @Mock
    private ExchangeRateXmlParser mockExchangeRateXmlParser;

    private ExchangeRateProvider exchangeRateProvider;

    private ExchangeRateXmlParser exchangeRateXmlParser;

    @BeforeEach
    void setup() {
        exchangeRateXmlParser = new ExchangeRateXmlParser(mockCurrencyRepository);
        exchangeRateProvider = new ExchangeRateProvider(mockHttpClient, exchangeRateXmlParser);
    }

    @Test
    void testFetchAllByDate_Success() throws IOException, InterruptedException, BadHttpClientRequestException,
            BadApiResponseException, FailedParsingException {
        LocalDate date = LocalDate.of(2024, 5, 10);
        String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<FxRates xmlns:xsi=\"http://www.w3.org/2021/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"FxRatesSchema.xsd\">\n"
                +
                "  <FxRate>\n" +
                "    <Tp>LT</Tp>\n" +
                "    <Dt>2024-05-10</Dt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>EUR</Ccy>\n" +
                "      <Amt>1.0000</Amt>\n" +
                "    </CcyAmt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>USD</Ccy>\n" +
                "      <Amt>1.0926</Amt>\n" +
                "    </CcyAmt>\n" +
                "  </FxRate>\n" +
                "  <FxRate>\n" +
                "    <Tp>LT</Tp>\n" +
                "    <Dt>2024-05-10</Dt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>EUR</Ccy>\n" +
                "      <Amt>1.0000</Amt>\n" +
                "    </CcyAmt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>JPY</Ccy>\n" +
                "      <Amt>142.75</Amt>\n" +
                "    </CcyAmt>\n" +
                "  </FxRate>\n" +
                "</FxRates>";

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

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn(xmlResponse);
        when(mockCurrencyRepository.findAllByCodeIn(List.of("USD", "JPY")))
                .thenReturn(List.of(currencyUSD, currencyJPY));

        List<ExchangeRate> expectedExchangeRates = List.of(exchangeRateUSD, exchangeRateJPY);
        List<ExchangeRate> actualExchangeRates = exchangeRateProvider.fetchAllByDate(date);

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }

    @Test
    public void testFetchAllByDate_Non200Response() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        assertThrows(BadApiResponseException.class, () -> {
            exchangeRateProvider.fetchAllByDate(LocalDate.now());
        });
    }

    @Test
    void testFetchAllForCurrencyByDateBetween_Success()
            throws IOException, InterruptedException, BadHttpClientRequestException,
            BadApiResponseException, FailedParsingException {
        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 10);
        Currency currency = Currency.builder()
                .id(1L)
                .code("USD")
                .name("United States dollar")
                .minorUnits(2)
                .build();
        String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<FxRates xmlns:xsi=\"http://www.w3.org/2021/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"FxRatesSchema.xsd\">\n"
                +
                "  <FxRate>\n" +
                "    <Tp>LT</Tp>\n" +
                "    <Dt>2024-05-01</Dt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>EUR</Ccy>\n" +
                "      <Amt>1.0000</Amt>\n" +
                "    </CcyAmt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>USD</Ccy>\n" +
                "      <Amt>1.0926</Amt>\n" +
                "    </CcyAmt>\n" +
                "  </FxRate>\n" +
                "  <FxRate>\n" +
                "    <Tp>LT</Tp>\n" +
                "    <Dt>2024-05-02</Dt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>EUR</Ccy>\n" +
                "      <Amt>1.0000</Amt>\n" +
                "    </CcyAmt>\n" +
                "    <CcyAmt>\n" +
                "      <Ccy>USD</Ccy>\n" +
                "      <Amt>1.0930</Amt>\n" +
                "    </CcyAmt>\n" +
                "  </FxRate>\n" +
                "</FxRates>";

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

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn(xmlResponse);
        when(mockCurrencyRepository.findAllByCodeIn(List.of("USD")))
                .thenReturn(List.of(currency));

        List<ExchangeRate> actualExchangeRates = exchangeRateProvider.fetchAllForCurrencyByDateBetween(currency,
                fromDate, toDate);

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }

    @Test
    void testFetchAllForCurrencyByDateBetween_Non200Response() throws Exception {
        Currency currency = Currency.builder()
                .id(1L)
                .code("USD")
                .name("United States dollar")
                .minorUnits(2)
                .build();
        LocalDate fromDate = LocalDate.of(2024, 5, 1);
        LocalDate toDate = LocalDate.of(2024, 5, 10);

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        assertThrows(BadApiResponseException.class, () -> {
            exchangeRateProvider.fetchAllForCurrencyByDateBetween(currency, fromDate, toDate);
        });
    }
}

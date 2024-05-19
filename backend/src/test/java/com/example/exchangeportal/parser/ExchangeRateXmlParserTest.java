package com.example.exchangeportal.parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.repository.CurrencyRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateXmlParserTest {

    @Mock
    private CurrencyRepository mockCurrencyRepository;

    @InjectMocks
    private ExchangeRateXmlParser parser;

    @Test
    void testParseAll_Success() throws FailedParsingException {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
        LocalDate date = LocalDate.parse("2024-05-10");
        Currency currencyUSD = Currency.builder()
                .id(1L)
                .code("USD")
                .name("United Stated dollar")
                .minorUnits(2)
                .build();
        Currency currencyJPY = Currency.builder()
                .id(2L)
                .code("JPY")
                .name("Japanese yen")
                .minorUnits(2)
                .build();

        when(mockCurrencyRepository.findAllByCodeIn(List.of("USD", "JPY")))
                .thenReturn(List.of(currencyUSD, currencyJPY));

        ExchangeRate expectedExchangeRateUSD = ExchangeRate.builder()
                .currency(currencyUSD)
                .rate(1.0926)
                .date(date)
                .build();
        ExchangeRate expectedExchangeRateJPY = ExchangeRate.builder()
                .currency(currencyJPY)
                .rate(142.75)
                .date(date)
                .build();
        List<ExchangeRate> expectedExchangeRates = List.of(expectedExchangeRateUSD, expectedExchangeRateJPY);
        List<ExchangeRate> actualExchangeRates = parser.parseAll(xmlData);

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }

    @Test
    void testParseAll_NonExistingCurrencyCode() {
        String NON_EXISTING_CURRENCY_CODE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "      <Ccy>XYZ</Ccy>\n" +
                "      <Amt>142.75</Amt>\n" +
                "    </CcyAmt>\n" +
                "  </FxRate>\n" +
                "</FxRates>";

        assertThrows(RuntimeException.class, () -> parser.parseAll(NON_EXISTING_CURRENCY_CODE));
    }
}

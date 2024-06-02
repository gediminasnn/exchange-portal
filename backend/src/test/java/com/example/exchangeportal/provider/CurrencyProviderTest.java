package com.example.exchangeportal.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.exchangeportal.apiclient.LbApiClient;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.parser.CurrencyXmlParser;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CurrencyProviderTest {
    @Mock
    private LbApiClient mockLbApiClient;

    private CurrencyProvider currencyProvider;

    private CurrencyXmlParser currencyXmlParser;

    @BeforeEach
    void setup() {
        currencyXmlParser = new CurrencyXmlParser();
        currencyProvider = new CurrencyProvider(mockLbApiClient, currencyXmlParser);
    }

    @Test
    public void testFetchAll_Success() throws Exception {
        String validXml = """
                <CcyTbl xmlns="http://www.lb.lt/WebServices/FxRates">
                    <CcyNtry>
                        <Ccy>AED</Ccy>
                        <CcyNm lang="LT">Jungtinių Arabų Emiratų dirhamas</CcyNm>
                        <CcyNm lang="EN">UAE dirham</CcyNm>
                        <CcyNbr>784</CcyNbr>
                        <CcyMnrUnts>2</CcyMnrUnts>
                    </CcyNtry>
                </CcyTbl>
                """;

        when(mockLbApiClient.fetchCurrencyListData()).thenReturn(validXml);

        List<Currency> expectedCurrencies = Arrays.asList(Currency.builder()
                .code("AED")
                .name("UAE dirham")
                .minorUnits(2)
                .build());
        List<Currency> actualCurrencies = currencyProvider.fetchAll();
        assertEquals(expectedCurrencies, actualCurrencies);
    }
}

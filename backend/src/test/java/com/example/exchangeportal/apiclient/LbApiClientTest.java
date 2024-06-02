package com.example.exchangeportal.apiclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.exchangeportal.exception.BadHttpClientRequestException;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class LbApiClientTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    private LbApiClient lbApiClient;

    @BeforeEach
    void setup() {
        lbApiClient = new LbApiClient(mockHttpClient);
    }

    @Test
    public void testFetchCurrencyListData_Success()
            throws IOException, InterruptedException, BadHttpClientRequestException {
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

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn(validXml);

        String result = lbApiClient.fetchCurrencyListData();
        assertEquals(validXml, result);
    }

    @Test
    public void testFetchCurrencyListData_IOException() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("Simulated IOException"));

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchCurrencyListData();
        });
    }

    @Test
    public void testFetchCurrencyListData_InterruptedException() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new InterruptedException("Simulated InterruptedException"));

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchCurrencyListData();
        });
    }

    @Test
    public void testFetchCurrencyListData_Non200Response() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchCurrencyListData();
        });
    }

    @Test
    public void testFetchExchangeRatesData_Success()
            throws IOException, InterruptedException, BadHttpClientRequestException {
        String date = "2024-05-10";
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
                </FxRates>
                """;

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn(xmlResponse);

        String result = lbApiClient.fetchExchangeRatesData(date);
        assertEquals(xmlResponse, result);
    }

    @Test
    public void testFetchExchangeRatesData_IOException() throws IOException, InterruptedException {
        String date = "2024-05-10";
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("Simulated IOException"));

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchExchangeRatesData(date);
        });
    }

    @Test
    public void testFetchExchangeRatesData_InterruptedException() throws IOException, InterruptedException {
        String date = "2024-05-10";
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new InterruptedException("Simulated InterruptedException"));

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchExchangeRatesData(date);
        });
    }

    @Test
    public void testFetchExchangeRatesData_Non200Response() throws IOException, InterruptedException {
        String date = "2024-05-10";

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchExchangeRatesData(date);
        });
    }

    @Test
    public void testFetchExchangeRatesForCurrencyData_Success()
            throws IOException, InterruptedException, BadHttpClientRequestException {
        String currencyCode = "USD";
        String fromDate = "2024-05-01";
        String toDate = "2024-05-10";
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
                </FxRates>
                """;

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn(xmlResponse);

        String result = lbApiClient.fetchExchangeRatesForCurrencyData(currencyCode, fromDate, toDate);
        assertEquals(xmlResponse, result);
    }

    @Test
    public void testFetchExchangeRatesForCurrencyData_IOException() throws IOException, InterruptedException {
        String currencyCode = "USD";
        String fromDate = "2024-05-01";
        String toDate = "2024-05-10";
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("Simulated IOException"));

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchExchangeRatesForCurrencyData(currencyCode, fromDate, toDate);
        });
    }

    @Test
    public void testFetchExchangeRatesForCurrencyData_InterruptedException() throws IOException, InterruptedException {
        String currencyCode = "USD";
        String fromDate = "2024-05-01";
        String toDate = "2024-05-10";
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new InterruptedException("Simulated InterruptedException"));

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchExchangeRatesForCurrencyData(currencyCode, fromDate, toDate);
        });
    }

    @Test
    public void testFetchExchangeRatesForCurrencyData_Non200Response() throws IOException, InterruptedException {
        String currencyCode = "USD";
        String fromDate = "2024-05-01";
        String toDate = "2024-05-10";

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);

        assertThrows(BadHttpClientRequestException.class, () -> {
            lbApiClient.fetchExchangeRatesForCurrencyData(currencyCode, fromDate, toDate);
        });
    }
}

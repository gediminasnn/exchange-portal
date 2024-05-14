package com.example.exchangeportal.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.service.parser.CurrencyXmlParser;
import com.example.exchangeportal.service.provider.CurrencyXmlProvider;

@SpringBootTest
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyXmlProvider currencyProvider;

    @Mock
    private CurrencyXmlParser currencyXmlParser;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAndSaveCurrenciesFromApi_Success()
            throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        String xmlData = "<sampleXml>Some XML data</sampleXml>";
        List<Currency> currencies = Arrays.asList(new Currency(1L, "USD", "US Dollar", 2));

        when(currencyProvider.fetchCurrencyListXml()).thenReturn(xmlData);
        when(currencyXmlParser.parse(xmlData)).thenReturn(currencies);

        currencyService.fetchAndSaveCurrenciesFromApi();

        verify(currencyProvider).fetchCurrencyListXml();
        verify(currencyXmlParser).parse(xmlData);
        verify(currencyRepository).saveAll(currencies);
    }

    @Test
    void fetchAndSaveCurrenciesFromApi_ExceptionThrown()
            throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        when(currencyProvider.fetchCurrencyListXml()).thenThrow(new IOException());

        assertThrows(IOException.class, () -> {
            currencyService.fetchAndSaveCurrenciesFromApi();
        });

        verify(currencyProvider).fetchCurrencyListXml();
        verifyNoInteractions(currencyXmlParser);
        verifyNoInteractions(currencyRepository);
    }
}

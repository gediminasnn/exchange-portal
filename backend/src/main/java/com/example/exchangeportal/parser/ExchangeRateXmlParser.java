package com.example.exchangeportal.parser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.repository.CurrencyRepository;

@Component
public class ExchangeRateXmlParser {
    @Autowired
    private CurrencyRepository currencyRepository;

    public ExchangeRateXmlParser(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<ExchangeRate> parseAll(String xmlData) throws FailedParsingException {
        Document document = parseXmlData(xmlData);
        NodeList nodeList = document.getElementsByTagName("FxRate");
        List<String> currencyCodes = extractCurrencyCodes(nodeList);
        Map<String, Currency> currencyMap = fetchCurrencies(currencyCodes);
        return createExchangeRates(nodeList, currencyMap);
    }

    private Document parseXmlData(String xmlData) throws FailedParsingException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error while configuring xml parser", e);
        }

        try {
            return builder.parse(new java.io.ByteArrayInputStream(xmlData.getBytes()));
        } catch (SAXException | IOException e) {
            throw new FailedParsingException("Error occurred while parsing the xml data.", e);
        }
    }

    private List<String> extractCurrencyCodes(NodeList nodeList) {
        List<String> currencyCodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            NodeList ccyAmtList = element.getElementsByTagName("CcyAmt");
            String currencyCode = ((Element) ccyAmtList.item(1)).getElementsByTagName("Ccy").item(0).getTextContent();
            if (!currencyCodes.contains(currencyCode)) {
                currencyCodes.add(currencyCode);
            }
        }
        return currencyCodes;
    }

    private Map<String, Currency> fetchCurrencies(List<String> currencyCodes) {
        List<Currency> currencies = currencyRepository.findAllByCodeIn(currencyCodes);
        return currencies.stream().collect(Collectors.toMap(Currency::getCode, currency -> currency));
    }

    private List<ExchangeRate> createExchangeRates(NodeList nodeList, Map<String, Currency> currencyMap) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            ExchangeRate rate = processExchangeRateElement(element, currencyMap);
            exchangeRates.add(rate);
        }
        return exchangeRates;
    }

    private ExchangeRate processExchangeRateElement(Element element, Map<String, Currency> currencyMap) {
        String dateString = element.getElementsByTagName("Dt").item(0).getTextContent();
        LocalDate date = LocalDate.parse(dateString);

        NodeList ccyAmtList = element.getElementsByTagName("CcyAmt");
        String currencyCode = ((Element) ccyAmtList.item(1)).getElementsByTagName("Ccy").item(0).getTextContent();
        double rateAmount = Double
                .parseDouble(((Element) ccyAmtList.item(1)).getElementsByTagName("Amt").item(0).getTextContent());

        Currency currency = currencyMap.get(currencyCode);
        if (currency == null) {
            throw new RuntimeException("Currency not found for code: " + currencyCode);
        }

        return ExchangeRate
                .builder()
                .currency(currency)
                .rate(rateAmount)
                .date(date)
                .build();
    }
}

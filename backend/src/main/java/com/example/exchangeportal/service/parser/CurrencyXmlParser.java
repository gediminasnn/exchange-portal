package com.example.exchangeportal.service.parser;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.FailedParsingException;

@Component
public class CurrencyXmlParser {

    public List<Currency> parseAll(String xmlData) throws FailedParsingException {
        Document document = buildDocumentFromXml(xmlData);
        return extractCurrencies(document);
    }

    protected Document buildDocumentFromXml(String xmlData) throws FailedParsingException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error while configuring xml parser", e);
        }

        Document document;
        try {
            document = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));
        } catch (SAXException | IOException e) {
            throw new FailedParsingException("Error occurred while parsing the xml data.", e);
        }
        document.getDocumentElement().normalize();
        return document;
    }

    protected List<Currency> extractCurrencies(Document document) {
        List<Currency> currencies = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName("CcyNtry");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            currencies.add(parseCurrencyElement(element));
        }
        return currencies;
    }

    private Currency parseCurrencyElement(Element element) {
        String code = element.getElementsByTagName("Ccy").item(0).getTextContent();
        String name = element.getElementsByTagName("CcyNm").item(1).getTextContent();
        int minorUnits = Integer.parseInt(element.getElementsByTagName("CcyMnrUnts").item(0).getTextContent());

        return Currency.builder()
                .code(code)
                .name(name)
                .minorUnits(minorUnits)
                .build();
    }
}

package com.example.exchangeportal.service.parser;

import com.example.exchangeportal.entity.Currency;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyXmlParserTest {

    private final CurrencyXmlParser parser = new CurrencyXmlParser();

    @Test
    void testParse_Success() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
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

        Document document = parser.buildDocumentFromXml(validXml);
        List<Currency> currencies = parser.extractCurrencies(document);

        assertFalse(currencies.isEmpty());
        assertEquals("AED", currencies.get(0).getCode());
        assertEquals("UAE dirham", currencies.get(0).getName());
        assertEquals(2, currencies.get(0).getMinorUnits());
    }
}

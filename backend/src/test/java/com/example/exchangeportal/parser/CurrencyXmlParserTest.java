package com.example.exchangeportal.parser;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.FailedParsingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class CurrencyXmlParserTest {

    private final CurrencyXmlParser currencyXmlParser;

    public CurrencyXmlParserTest() {
        currencyXmlParser = new CurrencyXmlParser();
    }

    @Test
    void testParseAll_Success() throws FailedParsingException {
        String validXml = """
                <CcyTbl xmlns="http://www.lb.lt/WebServices/FxRates">
                    <CcyNtry>
                        <Ccy>AED</Ccy>
                        <CcyNm lang="LT">Jungtinių Arabų Emiratų dirhamas</CcyNm>
                        <CcyNm lang="EN">UAE dirham</CcyNm>
                        <CcyNbr>784</CcyNbr>
                        <CcyMnrUnts>2</CcyMnrUnts>
                    </CcyNtry>
                    <CcyNtry>
                        <Ccy>AFN</Ccy>
                        <CcyNm lang="LT">Afganistano afganis</CcyNm>
                        <CcyNm lang="EN">Afghani</CcyNm>
                        <CcyNbr>971</CcyNbr>
                        <CcyMnrUnts>2</CcyMnrUnts>
                    </CcyNtry>
                </CcyTbl>
                """;

        List<Currency> expectedCurrencies = List.of(
                Currency.builder().code("AED").name("UAE dirham").minorUnits(2).build(),
                Currency.builder().code("AFN").name("Afghani").minorUnits(2).build());

        List<Currency> actualCurrencies = currencyXmlParser.parseAll(validXml);
        assertEquals(expectedCurrencies, actualCurrencies);
    }
}

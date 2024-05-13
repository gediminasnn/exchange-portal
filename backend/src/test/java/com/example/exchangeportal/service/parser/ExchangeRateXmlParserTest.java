package com.example.exchangeportal.service.parser;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.ExchangeRate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateXmlParserTest {

	private ExchangeRateXmlParser parser = new ExchangeRateXmlParser();

	@Test
	void testParseValidXML() throws SAXException, IOException, ParserConfigurationException {
		String xmlData = "<FxRates><FxRate><Dt>2024-05-10</Dt><CcyAmt><Ccy>EUR</Ccy><Amt>1</Amt></CcyAmt><CcyAmt><Ccy>USD</Ccy><Amt>1.05</Amt></CcyAmt></FxRate></FxRates>";
		LocalDate date = LocalDate.parse("2024-05-10");
		List<ExchangeRate> result = parser.parse(xmlData);
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals("USD", result.get(0).getCurrencyCode());
		assertEquals(1.05, result.get(0).getRate());
		assertEquals(date, result.get(0).getDate());
	}

	@Test
	void testParseMultipleFxRates() throws SAXException, IOException, ParserConfigurationException {
		String multipleRatesXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<FxRates xmlns:xsi=\"http://www.w3.org/2021/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"FxRatesSchema.xsd\">\n"
				+
				"  <FxRate>\n" + // First FxRate
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
				"  <FxRate>\n" + // Second FxRate
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
		List<ExchangeRate> exchangeRates = parser.parse(multipleRatesXml);

		assertEquals(2, exchangeRates.size());

		// Assertions for the first FxRate element
		ExchangeRate rate1 = exchangeRates.get(0);
		assertNull(rate1.getId());
		assertEquals("USD", rate1.getCurrencyCode());
		assertEquals(1.0926, rate1.getRate());
		assertEquals(date, rate1.getDate());

		// Assertions for the second FxRate element
		ExchangeRate rate2 = exchangeRates.get(1);
		assertNull(rate2.getId());
		assertEquals("JPY", rate2.getCurrencyCode());
		assertEquals(142.75, rate2.getRate());
		assertEquals(date, rate2.getDate());
	}

	@Test
	void testParseEmptyXML() throws SAXException, IOException, ParserConfigurationException {
		String xmlData = "<FxRates></FxRates>";
		List<ExchangeRate> result = parser.parse(xmlData);
		assertTrue(result.isEmpty());
	}
}

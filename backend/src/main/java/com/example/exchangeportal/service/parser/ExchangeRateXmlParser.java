package com.example.exchangeportal.service.parser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.exchangeportal.entity.ExchangeRate;

@Component
public class ExchangeRateXmlParser {
	public List<ExchangeRate> parse(String xmlData) throws SAXException, IOException, ParserConfigurationException {
		if (xmlData.isEmpty()) {
			throw new IllegalArgumentException("xmlData cannot be empty string");
		}

		List<ExchangeRate> exchangeRates = new ArrayList<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document;

		builder = factory.newDocumentBuilder();
		document = builder.parse(new java.io.ByteArrayInputStream(xmlData.getBytes()));
		document.getDocumentElement().normalize();
		NodeList nodeList = document.getElementsByTagName("FxRate");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			String dateString = element.getElementsByTagName("Dt").item(0).getTextContent();
			LocalDate date = LocalDate.parse(dateString);

			NodeList ccyAmtList = element.getElementsByTagName("CcyAmt");
			String targetCurrency = ((Element) ccyAmtList.item(1)).getElementsByTagName("Ccy").item(0).getTextContent();
			double targetAmount = Double
					.parseDouble(((Element) ccyAmtList.item(1)).getElementsByTagName("Amt").item(0).getTextContent());

			ExchangeRate rate = new ExchangeRate(null, targetCurrency, targetAmount, date);
			exchangeRates.add(rate);
		}
		return exchangeRates;
	}
}

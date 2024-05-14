package com.example.exchangeportal.service.parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
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
	private CurrencyRepository currencyRepository;

	@InjectMocks
	private ExchangeRateXmlParser parser;

	@Test
	void testParse_Success() throws Exception {
		LocalDate date = LocalDate.parse("2024-05-10");
		Currency currencyUSD = new Currency(1L, "USD", "United States dollar", 2);
		Currency currencyJPY = new Currency(2L, "JPY", "Japanese yen", 0);
		when(currencyRepository.findByCodeIn(List.of("USD", "JPY")))
				.thenReturn(List.of(currencyUSD, currencyJPY));

		String VALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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

		List<ExchangeRate> exchangeRates = parser.parse(VALID_XML);

		assertEquals(2, exchangeRates.size());
		ExchangeRate rateUSD = exchangeRates.get(0);
		assertNull(rateUSD.getId());
		assertEquals(currencyUSD, rateUSD.getCurrency());
		assertEquals(1.0926, rateUSD.getRate());
		assertEquals(date, rateUSD.getDate());

		ExchangeRate rateJPY = exchangeRates.get(1);
		assertNull(rateJPY.getId());
		assertEquals(currencyJPY, rateJPY.getCurrency());
		assertEquals(142.75, rateJPY.getRate());
		assertEquals(date, rateJPY.getDate());
	}

	@Test
	void testParse_NonExistingCurrencyCode() throws Exception {
		String NON_EXISTING_CURRENCY_CODE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<FxRates xmlns:xsi=\"http://www.w3.org/2021/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"FxRatesSchema.xsd\">\n"
				+
				"  <FxRate>\n" +
				"    <Tp>LT</Tp>\n" +
				"    <Dt>2024-05-10</Dt>\n" +
				"    <CcyAmt>\n" +
				"      <Ccy>XYZ</Ccy>\n" + // Non-existent currency code
				"      <Amt>1.0000</Amt>\n" +
				"    </CcyAmt>\n" +
				"  </FxRate>\n" +
				"</FxRates>";

		assertThrows(RuntimeException.class, () -> parser.parse(NON_EXISTING_CURRENCY_CODE));
	}
}

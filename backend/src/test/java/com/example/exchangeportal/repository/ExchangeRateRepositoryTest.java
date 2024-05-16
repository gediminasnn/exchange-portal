package com.example.exchangeportal.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.entity.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class ExchangeRateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    public void findAllWithLatestDate_ShouldReturnOnlyLatestDates() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        Currency usd = Currency.builder().code("USD").name("United States Dollar").minorUnits(2).build();
        Currency gbp = Currency.builder().code("GBP").name("British Pound").minorUnits(2).build();
        entityManager.persist(usd);
        entityManager.persist(gbp);

        ExchangeRate usdExchangeRate = ExchangeRate.builder().currency(usd).rate(1.1).date(today).build();
        ExchangeRate gbpExchangeRate = ExchangeRate.builder().currency(gbp).rate(0.85).date(today).build();
        ExchangeRate usdExchangeRateYesterday = ExchangeRate.builder().currency(usd).rate(1.11).date(yesterday).build();
        ExchangeRate gbpExchangeRateYesterday = ExchangeRate.builder().currency(gbp).rate(0.86).date(yesterday).build();
        entityManager.persist(usdExchangeRate);
        entityManager.persist(gbpExchangeRate);
        entityManager.persist(usdExchangeRateYesterday);
        entityManager.persist(gbpExchangeRateYesterday);
        entityManager.flush();

        List<ExchangeRate> expectedExchangeRates = List.of(usdExchangeRate, gbpExchangeRate);
        List<ExchangeRate> actualExchangeRates = exchangeRateRepository.findAllWithLatestDate();

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }
}

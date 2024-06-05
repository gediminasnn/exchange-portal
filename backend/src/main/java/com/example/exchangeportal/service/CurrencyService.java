package com.example.exchangeportal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.example.exchangeportal.dto.CurrencyDto;
import com.example.exchangeportal.dto.ExchangeRateDto;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.repository.CurrencyRepository;
import com.example.exchangeportal.provider.CurrencyProvider;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyProvider currencyProvider;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ModelMapper modelMapper;

    public void fetchAndSaveCurrenciesFromApi() throws ApiException, ParsingException {
        currencyRepository.saveAll(currencyProvider.fetchAll());
    }

    public CurrencyDto getCurrencyWithExchangeRates(Long id, LocalDate fromDate, LocalDate toDate)
            throws ApiException, ParsingException {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Couldn't find currency with id: " + id));

        List<ExchangeRateDto> exchangeRateDtos = exchangeRateService.getAndPopulateMissingExchangeRatesForCurrency(
                currency, fromDate, toDate);

        CurrencyDto currencyDto = modelMapper.map(currency, CurrencyDto.class);

        currencyDto.setExchangeRates(exchangeRateDtos);

        return currencyDto;
    }
}

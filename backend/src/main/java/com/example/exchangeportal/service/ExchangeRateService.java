package com.example.exchangeportal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import com.example.exchangeportal.dto.ExchangeRateDto;
import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.BadApiResponseException;
import com.example.exchangeportal.exception.BadHttpClientRequestException;
import com.example.exchangeportal.exception.FailedParsingException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.provider.ExchangeRateProvider;
import com.example.exchangeportal.repository.ExchangeRateRepository;
import com.example.exchangeportal.util.DateUtils;
import jakarta.validation.constraints.NotNull;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateProvider exchangeRateProvider;

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private ModelMapper modelMapper;

    public void fetchAndSaveExchangeRatesFromApi() throws ApiException, ParsingException {
        exchangeRateRepository.saveAll(exchangeRateProvider.fetchAllByDate(LocalDate.now()));
    }

    public List<ExchangeRateDto> getLatestExchangeRates() {
        return exchangeRateRepository.findAllWithLatestDate()
                .stream()
                .map(rate -> modelMapper.map(rate, ExchangeRateDto.class))
                .collect(Collectors.toList());
    }

    public List<ExchangeRateDto> getAndPopulateMissingExchangeRatesForCurrency(Currency currency,
            @NotNull LocalDate fromDate,
            @NotNull LocalDate toDate)
            throws BadHttpClientRequestException, BadApiResponseException, FailedParsingException {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAllByCurrencyAndDateBetween(
                currency, fromDate, toDate);

        List<LocalDate> missingDates = this.dateUtils.findMissingDates(exchangeRates, fromDate, toDate);

        if (!missingDates.isEmpty()) {
            List<ExchangeRate> exchangeRatesFromApi = exchangeRateProvider.fetchAllForCurrencyByDateBetween(
                    currency,
                    fromDate, toDate);

            List<ExchangeRate> missingExchangeRates = exchangeRatesFromApi.stream()
                    .filter(rate -> missingDates.contains(rate.getDate()))
                    .collect(Collectors.toList());

            exchangeRateRepository.saveAll(missingExchangeRates);

            exchangeRates.addAll(missingExchangeRates);
        }

        exchangeRates.sort((rate1, rate2) -> rate2.getDate().compareTo(rate1.getDate()));

        return exchangeRates.stream()
                .map(rate -> modelMapper.map(rate, ExchangeRateDto.class))
                .collect(Collectors.toList());
    }
}

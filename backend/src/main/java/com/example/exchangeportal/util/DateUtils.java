package com.example.exchangeportal.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.exchangeportal.entity.ExchangeRate;

@Component
public class DateUtils {

    /**
     * Finds missing dates in the range between startDate and endDate.
     *
     * @param exchangeRates the list of existing exchange rates
     * @param fromDate              the start date
     * @param toDate                the end date
     * @return a list of missing dates
     */
    public List<LocalDate> findMissingDates(List<ExchangeRate> exchangeRates, LocalDate fromDate,
            LocalDate toDate) {
        Set<LocalDate> existingDates = extractDates(exchangeRates);
        List<LocalDate> allDatesInRange = generateAllDatesInRange(fromDate, toDate);
        return findMissingDates(existingDates, allDatesInRange);
    }

    /**
     * Extracts dates from a list of exchange rates.
     *
     * @param exchangeRates the list of exchange rates
     * @return a set of dates
     */
    public static Set<LocalDate> extractDates(List<ExchangeRate> exchangeRates) {
        return exchangeRates.stream()
                .map(ExchangeRate::getDate)
                .collect(Collectors.toSet());
    }

    /**
     * Generates all dates between the given start and end dates, inclusive.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return a list of all dates in the range
     */
    public static List<LocalDate> generateAllDatesInRange(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
    }

    /**
     * Finds missing dates between the existing dates and the full date range.
     *
     * @param existingDates   the set of existing dates
     * @param allDatesInRange the list of all dates in the range
     * @return a list of missing dates
     */
    public static List<LocalDate> findMissingDates(Set<LocalDate> existingDates, List<LocalDate> allDatesInRange) {
        return allDatesInRange.stream()
                .filter(date -> !existingDates.contains(date))
                .collect(Collectors.toList());
    }
}

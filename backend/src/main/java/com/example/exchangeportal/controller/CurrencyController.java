package com.example.exchangeportal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exchangeportal.entity.Currency;
import com.example.exchangeportal.exception.ApiException;
import com.example.exchangeportal.exception.ParsingException;
import com.example.exchangeportal.service.CurrencyService;
import com.example.exchangeportal.validation.ConsistentDateBetweenParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/api/currencies")
@ResponseStatus(HttpStatus.OK)
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/{id}/exchange-rates")
    @ConsistentDateBetweenParameters(fromDatePosition = 2, toDatePosition = 3)
    public Currency index(
            @PathVariable("id") Long id,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate)
            throws ApiException, ParsingException {
        return currencyService.getCurrencyWithExchangeRates(id, fromDate, toDate);
    }
}

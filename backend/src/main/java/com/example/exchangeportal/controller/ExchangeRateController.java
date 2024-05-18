package com.example.exchangeportal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.exchangeportal.entity.ExchangeRate;
import com.example.exchangeportal.service.ExchangeRateService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/exchange-rates")
@ResponseStatus(HttpStatus.OK)
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping
    public List<ExchangeRate> show() {
        return exchangeRateService.getLatestExchangeRates();
    }
}

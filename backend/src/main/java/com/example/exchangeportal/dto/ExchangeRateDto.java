package com.example.exchangeportal.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ExchangeRateDto {
    private double rate;
    private LocalDate date;
    private CurrencyDto currency;

    public ExchangeRateDto() {
    }

    public ExchangeRateDto(double rate, LocalDate date, CurrencyDto currency) {
        this.rate = rate;
        this.date = date;
        this.currency = currency;
    }
}

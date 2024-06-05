package com.example.exchangeportal.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CurrencyDto {
    private Long id;
    private String code;
    private String name;
    private List<ExchangeRateDto> exchangeRates;

    public CurrencyDto() {
    }

    public CurrencyDto(Long id, String code, String name, List<ExchangeRateDto> exchangeRates) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.exchangeRates = exchangeRates;
    }
}

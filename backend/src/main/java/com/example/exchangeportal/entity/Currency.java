package com.example.exchangeportal.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Currency {
    @Id
    @GeneratedValue()
    private Long id;

    @NotBlank(message = "Currency code cannot be empty")
    private String code;

    @NotBlank(message = "Currency name cannot be empty")
    private String name;

    @NotNull(message = "Currency minor units cannot be null")
    private int minorUnits;

    @OneToMany(mappedBy = "currency")
    @Transient
    private List<ExchangeRate> exchangeRates;

    @Builder
    public Currency(Long id, String code, String name, int minorUnits) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.minorUnits = minorUnits;
    }
}

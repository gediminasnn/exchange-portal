package com.example.exchangeportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
}

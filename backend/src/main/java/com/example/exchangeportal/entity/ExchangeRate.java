package com.example.exchangeportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ExchangeRate {
	@Id
	@GeneratedValue
	private Long id;

	@NotBlank(message = "Currency code cannot be empty")
	private String currencyCode;

	@NotNull(message = "Rate cannot be null")
	private double rate;

	@NotNull(message = "Date cannot be empty")
	private Date date;
}

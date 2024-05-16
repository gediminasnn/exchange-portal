package com.example.exchangeportal.repository;

import com.example.exchangeportal.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    /**
     * Finds all exchange rates where the date
     * is the most recent (latest) date in the database.
     *
     * @return A list of exchange rates matching the criteria.
     */
    @Query("SELECT er FROM ExchangeRate er WHERE er.date = (SELECT MAX(er2.date) FROM ExchangeRate er2)")
    List<ExchangeRate> findAllWithLatestDate();
}

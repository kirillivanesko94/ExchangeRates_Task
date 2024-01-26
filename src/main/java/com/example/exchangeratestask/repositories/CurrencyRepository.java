package com.example.exchangeratestask.repositories;

import com.example.exchangeratestask.model.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CurrencyRepository extends CrudRepository<Currency, String> {
    @Query("SELECT c FROM Currency c WHERE c.numCode = :numCode AND c.date = :date")
    Currency findCurrencyByNumCodeAndDate(@Param("numCode") String numCode, @Param("date") String date);
}

package com.example.exchangeratestask.repositories;

import com.example.exchangeratestask.model.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, String> {
}

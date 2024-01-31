package com.example.exchangeratestask.controller;

import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.service.ExchangeRatesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exchange-rates")
public class ExchangeRatesController {
    private final ExchangeRatesService service;

    public ExchangeRatesController(ExchangeRatesService service) {
        this.service = service;
    }

    @GetMapping("{num_code}/{date}")
    Currency findRate(@PathVariable("num_code") String numCode, @PathVariable("date") String date) {
        return service.findCurrencyByNumCode(numCode, date);
    }
    @GetMapping("convert-rouble-in-rate/{num_code}/{date}/{countOfRouble}")
    String convertRoubleInRate(@PathVariable("num_code") String numCode,
                               @PathVariable("date") String date,
                               @PathVariable("countOfRouble") Long countOfRouble) {
        return service.convertRoubleInCurrency(numCode, date, countOfRouble);
    }

    @PostMapping("save")
    void save() {
        service.saveCurrencyRates();
    }

}

package com.example.exchangeratestask.controller;

import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.service.ExchangeRatesService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("exchange-rates")
public class ExchangeRatesController {
   private final ExchangeRatesService service;

    public ExchangeRatesController(ExchangeRatesService service) {
        this.service = service;
    }
    @GetMapping("get-all-rates")
    Map<String, Currency> getCurrencyRate() throws IOException {
        return service.parseJsonToMap();
    }
    @GetMapping("{rate}")
    Currency findRate(@PathVariable String rate) throws IOException {

        return service.findCurrencyRate(rate);
    }
}

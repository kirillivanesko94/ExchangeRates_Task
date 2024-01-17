package com.example.exchangeratestask.controller;

import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.model.CurrencyDto;
import com.example.exchangeratestask.service.ExchangeRatesService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("exchange-rates")
public class ExchangeRatesController {
    private final ExchangeRatesService service;

    public ExchangeRatesController(ExchangeRatesService service) {
        this.service = service;
    }

    @GetMapping("get-all-rates")
    Map<String, CurrencyDto> getCurrencyRate() throws IOException {
        return service.parseJsonToMap();
    }

    @GetMapping("{rate}")
    CurrencyDto findRate(@PathVariable String rate) throws IOException {

        return service.findCurrencyRate(rate);
    }

    @PostMapping("save")
    void save() {
        service.saveCurrencyRates();
    }
}

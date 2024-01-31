package com.example.exchangeratestask.controller;

import com.example.exchangeratestask.exception.CurrencyNotFoundException;
import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.service.ExchangeRatesService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class is responsible for processing requests from the user
 */
@RestController
@RequestMapping("exchange-rates")
public class ExchangeRatesController {
    private final ExchangeRatesService service;

    public ExchangeRatesController(ExchangeRatesService service) {
        this.service = service;
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    @GetMapping("{num_code}/{date}")
    Currency findRate(@PathVariable("num_code") String numCode, @PathVariable("date") String date) {
        try {
            return service.findCurrencyByNumCode(numCode, date);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json"
                    )}
            )
    })
    @GetMapping("convert-rouble-in-rate/{num_code}/{date}/{countOfRouble}")
    String convertRoubleInRate(@PathVariable("num_code") String numCode,
                               @PathVariable("date") String date,
                               @PathVariable("countOfRouble") Long countOfRouble) {
        return service.convertRoubleInCurrency(numCode, date, countOfRouble);
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json"
                    )}
            )
    })
    @PostMapping("save")
    void save() {
        service.saveCurrencyRates();
    }

}

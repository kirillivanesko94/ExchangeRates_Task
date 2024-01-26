package com.example.exchangeratestask.service;

import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.model.CurrencyDto;
import com.example.exchangeratestask.repositories.CurrencyRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.sauronsoftware.cron4j.Scheduler;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

@Service
public class ExchangeRatesService {
    private final String URL_RESOURCE = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CurrencyRepository repository;

    public ExchangeRatesService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public HashMap<String, CurrencyDto> parseJsonToMap() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JSONObject jsonObject = new JSONObject(getJson());
            JSONObject valuteJson = jsonObject.getJSONObject("Valute");

            HashMap<String, CurrencyDto> currencyMap = new HashMap<>();
            Iterator<String> keys = valuteJson.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject currencyJson = valuteJson.getJSONObject(key);

                CurrencyDto currencyDto = objectMapper.readValue(currencyJson.toString(), CurrencyDto.class);
                currencyMap.put(key, currencyDto);
            }

            return currencyMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJson() throws IOException {
        URL url = new URL(URL_RESOURCE);
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        JSONObject jsonObject = new JSONObject(result.toString());
        return jsonObject.toString();
    }
    public void saveCurrencyRates() {
        Map<String, CurrencyDto> currencyMap = parseJsonToMap();
        Scheduler scheduler = new Scheduler();

        for (CurrencyDto currencyDto : currencyMap.values()) {

            Currency currency = new Currency();
            currency.setId(currencyDto.getID());
            currency.setName(currencyDto.getName());
            currency.setNominal(currencyDto.getNominal());
            currency.setPrevious(currencyDto.getPrevious());
            currency.setValue(currencyDto.getValue());
            currency.setCharCode(currencyDto.getCharCode());
            currency.setNumCode(currencyDto.getNumCode());
            currency.setDate(LocalDate.now().format(FORMATTER));

            repository.save(currency);

            scheduler.schedule("0 9 * * *", new Runnable() {
                @Override
                public void run() {
                    saveCurrencyRates();
                }
            });
            scheduler.start();
        }
    }
    public Currency findCurrencyByNumCode(String numCode, String date) {
        return repository.findCurrencyByNumCodeAndDate(numCode,date);
    }
    public String convertRoubleInCurrency(String numCode, String date, Long countRouble) {
        Currency currency = findCurrencyByNumCode(numCode, date);
        return String.format("Количество рублей: %s Количество валюты: %s", countRouble, countRouble / currency.getValue());
    }
}

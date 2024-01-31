package com.example.exchangeratestask.service;

import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.model.CurrencyDto;
import com.example.exchangeratestask.repositories.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.sauronsoftware.cron4j.Scheduler;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.stream.Collectors;

@Service
public class ExchangeRatesService {
    private final String URL_RESOURCE = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CurrencyRepository repository;

    public ExchangeRatesService(CurrencyRepository repository) {
        this.repository = repository;
    }

    private HttpURLConnection openConnection(String url) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }

    private String getJson() throws IOException {
        HttpURLConnection connection = openConnection(URL_RESOURCE);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    public void saveCurrencyRates() {
        try {
            String json = getJson();
            JSONObject jsonObject = new JSONObject(json);
            JSONObject valuteJson = jsonObject.getJSONObject("Valute");
            ObjectMapper objectMapper = new ObjectMapper();

            Iterator<String> keys = valuteJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject currencyJson = valuteJson.getJSONObject(key);

                CurrencyDto currencyDto = objectMapper.readValue(currencyJson.toString(), CurrencyDto.class);

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
            }

            Scheduler scheduler = new Scheduler();
            scheduler.schedule("0 9 * * *", new Runnable() {
                @Override
                public void run() {
                    saveCurrencyRates();
                }
            });
            scheduler.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency findCurrencyByNumCode(String numCode, String date) {
        return repository.findCurrencyByNumCodeAndDate(numCode,date);
    }
    public String convertRoubleInCurrency(String numCode, String date, Long countRouble) {
        Currency currency = findCurrencyByNumCode(numCode, date);
        return String.format("Количество рублей: %s Количество валюты: %.2f", countRouble, countRouble / currency.getValue());
    }
}

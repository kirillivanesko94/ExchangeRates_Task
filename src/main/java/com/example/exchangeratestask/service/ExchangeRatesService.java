package com.example.exchangeratestask.service;

import com.example.exchangeratestask.exception.CurrencyNotFoundException;
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

/**
 * This class is responsible for the business logic of the application
 */
@Service
public class ExchangeRatesService {
    private final String URL_RESOURCE = "https://www.cbr-xml-daily.ru/daily_json.js";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CurrencyRepository repository;

    public ExchangeRatesService(CurrencyRepository repository) {
        this.repository = repository;
    }

    /**
     * This method opens a connection to a resource to receive data
     * @param url Resource address
     * @return connection
     * @throws IOException default exception
     */
    private HttpURLConnection openConnection(String url) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }

    /**
     * A method for getting a String variable from a data stream
     * @return The string received from the data stream
     * @throws IOException default exception
     */

    private String getJson() throws IOException {
        HttpURLConnection connection = openConnection(URL_RESOURCE);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    /**
     * A method for parsing data into an entity object and writing to a database
     */

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

    /**
     * The method for obtaining the exchange rate
     * @param numCode the required date
     * @param date the required date
     * @return {@link java.util.Currency}
     */
    public Currency findCurrencyByNumCode(String numCode, String date) {
        Currency currency = repository.findCurrencyByNumCodeAndDate(numCode, date);
        if (currency == null){
            throw new CurrencyNotFoundException(numCode);
        }
        return currency;
    }

    /**
     * The method for converting the ruble into a currency
     * @param numCode the required date
     * @param date the required date
     * @param countRouble The number of rubles required to convert into currency
     * @return A string containing information about the amount of rouble and currency
     */
    public String convertRoubleInCurrency(String numCode, String date, Long countRouble) {
        Currency currency = findCurrencyByNumCode(numCode, date);
        return String.format("Количество рублей: %s Количество валюты: %.2f", countRouble, countRouble / currency.getValue());
    }
}

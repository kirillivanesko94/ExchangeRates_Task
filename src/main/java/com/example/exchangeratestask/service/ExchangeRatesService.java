package com.example.exchangeratestask.service;

import com.example.exchangeratestask.model.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class ExchangeRatesService {
    String URL_RESOURCE = "https://www.cbr-xml-daily.ru/daily_json.js";
    public HashMap<String, Currency> parseJsonToMap() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Currency> currencyMap = objectMapper.readValue(getJson(), HashMap.class);
            return currencyMap;
        } catch (IOException e) {
            throw new RuntimeException();
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

    public Currency findCurrencyRate(String rate) {
        Map<String, Currency> result = parseJsonToMap();
        return result.get(rate);
    }
}

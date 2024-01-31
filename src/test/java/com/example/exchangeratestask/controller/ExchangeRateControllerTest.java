package com.example.exchangeratestask.controller;

import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.service.ExchangeRatesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRatesController.class)
public class ExchangeRateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRatesService service;
    private static final Currency USD = new Currency();

    static {
        USD.setId("R01235");
        USD.setNumCode("840");
        USD.setCharCode("USD");
        USD.setNominal(1);
        USD.setName("Доллар США");
        USD.setValue(88.6562);
        USD.setPrevious(88.2829);
        USD.setDate("26.01.2024");
     }


    @Test
    void findRateTest() throws Exception {
        when(service.findCurrencyByNumCode(anyString(), anyString())).thenReturn(USD);

        mockMvc.perform(get("/exchange-rates/{num_code}/{date}", "840", "26.01.2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("R01235"))
                .andExpect(jsonPath("$.numCode").value("840"))
                .andExpect(jsonPath("$.charCode").value("USD"))
                .andExpect(jsonPath("$.nominal").value(1))
                .andExpect(jsonPath("$.name").value("Доллар США"))
                .andExpect(jsonPath("$.value").value(88.6562))
                .andExpect(jsonPath("$.previous").value(88.2829))
                .andExpect(jsonPath("$.date").value("26.01.2024"));

        verify(service, times(1)).findCurrencyByNumCode(USD.getNumCode(), USD.getDate());
    }

    @Test
    void saveTest() throws Exception{

        mockMvc.perform(post("/exchange-rates/save")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).saveCurrencyRates();
    }

    @Test
    void convertRoubleInRateTest() throws Exception {
        String message = "Количество рублей: 10000 Количество валюты: 112,80";
        when(service.convertRoubleInCurrency(anyString(),anyString(), anyLong())).thenReturn(message);

        mockMvc.perform(get("/exchange-rates/convert-rouble-in-rate/{num_code}/{date}/{countOfRouble}",
                        "840", "26.01.2024", 10_000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(message));

        verify(service, times(1)).convertRoubleInCurrency(anyString(),anyString(), anyLong());
    }
}

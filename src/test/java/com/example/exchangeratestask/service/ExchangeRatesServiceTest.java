package com.example.exchangeratestask.service;

import com.example.exchangeratestask.exception.CurrencyNotFoundException;
import com.example.exchangeratestask.model.Currency;
import com.example.exchangeratestask.repositories.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ExchangeRatesServiceTest {
    @Mock
    private CurrencyRepository currencyRepository;
    @InjectMocks
    private ExchangeRatesService service;
    private final static Currency USD = new Currency();
    private final static Currency EUR = new Currency();
    private final static Currency CHF = new Currency();

    static {
        USD.setId("R01235");
        USD.setNumCode("840");
        USD.setCharCode("USD");
        USD.setNominal(1);
        USD.setName("Доллар США");
        USD.setValue(88.6562);
        USD.setPrevious(88.2829);
        USD.setDate("26.01.2024");

        EUR.setId("R01239");
        EUR.setNumCode("978");
        EUR.setCharCode("EUR");
        EUR.setNominal(1);
        EUR.setName("Евро");
        EUR.setValue(96.6365);
        EUR.setPrevious(96.1657);
        EUR.setDate("26.01.2024");

        CHF.setId("R01775");
        CHF.setNumCode("756");
        CHF.setCharCode("CHF");
        CHF.setNominal(1);
        CHF.setName("Швейцарский франк");
        CHF.setValue(102.6708);
        CHF.setPrevious(102.0494);
        CHF.setDate("26.01.2024");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("getCurrency")
    void testSaveCurrency(Currency currency) {
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        currencyRepository.save(currency);

        verify(currencyRepository, times(1)).save(any(Currency.class));

    }

    private static Stream<Arguments> getCurrency() {
        return Stream.of(
                Arguments.of(USD),
                Arguments.of(EUR),
                Arguments.of(CHF)
        );
    }

    @ParameterizedTest
    @MethodSource("getCurrencyNumCodeAndDate")
    void testFindCurrency(String numCode, String date, Currency expected) {
        when(currencyRepository.findCurrencyByNumCodeAndDate(anyString(), anyString())).thenReturn(expected);

        Currency result = service.findCurrencyByNumCode(numCode, date);

        assertEquals(expected, result);
        verify(currencyRepository, times(1)).findCurrencyByNumCodeAndDate(numCode, date);
    }

    private static Stream<Arguments> getCurrencyNumCodeAndDate() {
        return Stream.of(
                Arguments.of(USD.getNumCode(), USD.getDate(), USD),
                Arguments.of(EUR.getNumCode(), EUR.getDate(), EUR),
                Arguments.of(CHF.getNumCode(), CHF.getDate(), CHF)
        );
    }

    @Test
    void testFindCurrencyWithException() {
        when(currencyRepository.findCurrencyByNumCodeAndDate(anyString(), anyString())).thenReturn(null);

        assertThrows(CurrencyNotFoundException.class, () -> {
            service.findCurrencyByNumCode("1", "2");
        });
    }


    @ParameterizedTest
    @MethodSource("getArguments")
    void testConvertRoubleInCurrency(String numCode, String date, Currency currency, String expected) {
        when(currencyRepository.findCurrencyByNumCodeAndDate(numCode, date)).thenReturn(currency);

        String result = service.convertRoubleInCurrency(numCode, date, 10000L);

        assertEquals(expected, result);
        verify(currencyRepository, times(1)).findCurrencyByNumCodeAndDate(numCode, date);
    }

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of(USD.getNumCode(), USD.getDate(), USD,
                        String.format("Количество рублей: 10000 Количество валюты: %.2f", 10000 / USD.getValue())),
                Arguments.of(EUR.getNumCode(), EUR.getDate(), EUR,
                        String.format("Количество рублей: 10000 Количество валюты: %.2f", 10000 / EUR.getValue())),
                Arguments.of(CHF.getNumCode(), CHF.getDate(), CHF,
                        String.format("Количество рублей: 10000 Количество валюты: %.2f", 10000 / CHF.getValue()))
        );
    }

}

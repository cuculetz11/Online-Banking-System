package org.poo.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ExchangeRates {
    private final Map<CurrencyPair, Double> rates;
    private final Map<String, ArrayList<String>> currencies;

    public ExchangeRates() {
        this.rates = new HashMap<>();
        this.currencies = new HashMap<>();
    }

}

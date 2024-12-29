package org.poo.services;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class CurrencyExchangeService {
    /**
     * Efectuam schimbul valutar necesar
     * @param currencyPair perechea pentru care se realizaeza schimbul valutar
     * @param amount suma
     * @return returneaza suma schimbata
     */
    public double exchangeCurrency(final CurrencyPair currencyPair, final double amount) {
        if (currencyPair.getFrom().equals(currencyPair.getTo())) {
            return amount;
        }
        Map<CurrencyPair, Double> rates = Bank.getInstance().getExchangeRates().getRates();
        Map<String, ArrayList<String>> currencies = Bank.getInstance().getExchangeRates()
                .getCurrencies();
        if (rates.containsKey(currencyPair)) {
            return rates.get(currencyPair) * amount;
        }
        HashSet<String> visited = new HashSet<>();
        exchangeCurrencyDFS(currencyPair.getFrom(), currencyPair.getTo(), visited, 1, rates,
                currencies, currencyPair);
        double rate = rates.get(currencyPair);
        CurrencyPair inverse = new CurrencyPair(currencyPair.getTo(), currencyPair.getFrom());
        rates.put(inverse, 1 / rate);
        if (!currencies.containsKey(inverse.getFrom())) {
            currencies.put(inverse.getFrom(), new ArrayList<>());
        }
        currencies.get(inverse.getFrom()).add(inverse.getTo());
        return amount * rate;
    }

    /**
     * Adaug direct in banca rate-ul gasit precum si noua pereche pentru ca poate am nevoie mai
     * tarziu iarasi de ea
     * Facand acest map si introducand in el noua pereche precum si perchea inversa ma scuteste sa
     * fac iar dfs daca mai am nevoie de acest schimb valutar
     */
    public void exchangeCurrencyDFS(final String from, final String to,
                                    final HashSet<String> visited,
                                    final double rate, final Map<CurrencyPair, Double> rates,
                                    final Map<String, ArrayList<String>> currencies,
                                    final CurrencyPair searchedPair) {
        visited.add(from);
        if (from.equals(to)) {
            rates.put(searchedPair, rate);
            if (!currencies.containsKey(searchedPair.getFrom())) {
                currencies.put(searchedPair.getFrom(), new ArrayList<>());
            }
            currencies.get(searchedPair.getFrom()).add(to);
            return;
        }
        ArrayList<String> neighbours = currencies.get(from);
        for (String neighbour : neighbours) {
            if (!visited.contains(neighbour)) {
                CurrencyPair currencyPair = new CurrencyPair(from, neighbour);
                double newRate = rate * rates.get(currencyPair);
                exchangeCurrencyDFS(neighbour, to, visited, newRate, rates, currencies,
                        searchedPair);
                if (rates.containsKey(searchedPair)) {
                    return;
                }
            }
        }

    }
}

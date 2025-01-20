package org.poo.services.initialize;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.fileio.ExchangeInput;
import org.poo.services.BankMethods;

import java.util.ArrayList;
import java.util.Map;

/*
Adaug atat conversia data catt si inversa sa
 */
public class ExchangeRatesInitialize implements BankMethods {
    private final ExchangeInput[] exchangeInput;

    public ExchangeRatesInitialize(final ExchangeInput[] exchangeInput) {
        this.exchangeInput = exchangeInput;
    }

    /**
     * Initializeaza map-ul de valuta din banca
     * @param bank banca ca parametru
     */
    public void visit(final Bank bank) {
        for (ExchangeInput input : exchangeInput) {
            CurrencyPair currencyPair1 = new CurrencyPair(input.getFrom(), input.getTo());
            bank.getExchangeRates().getRates().put(currencyPair1, input.getRate());
            CurrencyPair currencyPair2 = new CurrencyPair(input.getTo(), input.getFrom());
            bank.getExchangeRates().getRates().put(currencyPair2, (1 / input.getRate()));
            Map<String, ArrayList<String>> currencies = bank.getExchangeRates().getCurrencies();
            if (!currencies.containsKey(input.getFrom())) {
                currencies.put(input.getFrom(), new ArrayList<>());
            }
            if (!currencies.containsKey(input.getTo())) {
                currencies.put(input.getTo(), new ArrayList<>());
            }
            currencies.get(input.getTo()).add(input.getFrom());
            currencies.get(input.getFrom()).add(input.getTo());
        }
    }
}

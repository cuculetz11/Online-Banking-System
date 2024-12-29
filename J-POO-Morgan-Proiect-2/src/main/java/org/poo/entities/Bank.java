package org.poo.entities;

import lombok.Getter;
import lombok.Setter;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public final class Bank {
    private static Bank instance = null;
    private LinkedHashMap<String, User> users;
    private Map<String, Account> accounts;
    private Map<String, Card> cards;
    private ExchangeRates exchangeRates;
    private Map<String, ArrayList<Transaction>> transactionHistory;
    private Map<String, Card> cardDeletedHistory;
    @Setter
    private int currentTimestamp;

    private Bank() {
        users = new LinkedHashMap<>();
        exchangeRates = new ExchangeRates();
        transactionHistory = new HashMap<>();
        accounts = new HashMap<>();
        cards = new HashMap<>();
        cardDeletedHistory = new HashMap<>();
    }

    /**
     * Obtine instanta singleton a bancii
     * @return obiectul unic ce reprezinta toata banca
     */
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    /**
     * Resetez toate campurile aceste clase singleton, altfel ar rmane de la test la test date
     */
    public void reset() {
        users = new LinkedHashMap<>();
        accounts = new HashMap<>();
        exchangeRates = new ExchangeRates();
        transactionHistory = new HashMap<>();
        cards = new HashMap<>();
        cardDeletedHistory = new HashMap<>();
    }

}

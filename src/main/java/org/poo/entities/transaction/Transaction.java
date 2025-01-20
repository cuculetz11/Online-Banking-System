package org.poo.entities.transaction;

import lombok.Getter;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;

import java.util.ArrayList;

@Getter
public class Transaction {
    private final int timestamp;
    private final String description;

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * Adauga tranzactia in istoricul user-ului
     * @param userEmail emailul user-ului
     */
    public void addToUserHistory(final String userEmail) {
        Bank.getInstance().getTransactionHistory().computeIfAbsent(userEmail,
                k -> new ArrayList<>());
        Bank.getInstance().getTransactionHistory().get(userEmail).add(this);
    }

    /**
     * Adauga tranzactia in istoricul contului
     * @param iban ibanul contului
     */
    public void addToAccountHistory(final String iban) {
        Account account = Bank.getInstance().getAccounts().get(iban);
        account.getTransactionsHistory().add(this);
    }
}

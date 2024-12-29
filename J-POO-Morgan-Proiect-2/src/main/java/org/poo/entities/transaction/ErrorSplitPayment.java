package org.poo.entities.transaction;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorSplitPayment extends Transaction {
    private final String currency;
    private final double amount;
    private final String error;
    private final List<String> involvedAccounts;

    public ErrorSplitPayment(final int timestamp, final String currency, final double amount,
                             final List<String> involvedAccounts, final String description,
                             final String error) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.error = error;
    }

}

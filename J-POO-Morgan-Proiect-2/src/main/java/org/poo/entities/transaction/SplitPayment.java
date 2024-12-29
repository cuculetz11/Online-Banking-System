package org.poo.entities.transaction;

import lombok.Getter;

import java.util.List;

@Getter
public class SplitPayment extends Transaction {
    private final String currency;
    private final double amount;
    private final List<String> involvedAccounts;

    public SplitPayment(final int timestamp, final String currency, final double amount,
                        final List<String> involvedAccounts, final String description) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
    }

}

package org.poo.entities.transaction;

import lombok.Getter;

import java.util.List;

@Getter
public class SplitPaymentEq extends Transaction {
    private final String currency;
    private double amount;
    private String splitPaymentType;
    private final List<String> involvedAccounts;

    public SplitPaymentEq(final int timestamp, final String currency,
                        final List<String> involvedAccounts, final String description,
                        final String splitPaymentType, double amount) {
        super(timestamp, description);
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.splitPaymentType = splitPaymentType;
        this.amount = amount;
    }
}

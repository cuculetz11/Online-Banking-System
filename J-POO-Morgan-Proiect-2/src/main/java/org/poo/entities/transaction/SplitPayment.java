package org.poo.entities.transaction;

import lombok.Getter;

import java.util.List;

@Getter
public class SplitPayment extends Transaction {
    private final String currency;

    private List<Double> amountForUsers;
    private String splitPaymentType;
    private final List<String> involvedAccounts;

    public SplitPayment(final int timestamp, final String currency,
                        final List<String> involvedAccounts, final String description,
                        final String splitPaymentType, final List<Double> amountForUsers) {
        super(timestamp, description);
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.splitPaymentType = splitPaymentType;
        this.amountForUsers = amountForUsers;
    }

}

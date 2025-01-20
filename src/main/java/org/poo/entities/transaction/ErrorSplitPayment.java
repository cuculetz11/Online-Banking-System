package org.poo.entities.transaction;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorSplitPayment extends Transaction {
    private final String currency;
    private final List<Double> amountForUsers;
    private final String splitPaymentType;
    private final String error;
    private final List<String> involvedAccounts;

    public ErrorSplitPayment(final int timestamp, final String currency,
                             final List<String> involvedAccounts, final String description,
                             final String splitPaymentType, final List<Double> amountForUsers,
                             final String error) {
        super(timestamp, description);
        this.currency = currency;
        this.involvedAccounts = involvedAccounts;
        this.error = error;
        this.splitPaymentType = splitPaymentType;
        this.amountForUsers = amountForUsers;
    }

}

package org.poo.entities.transaction;

import lombok.Getter;

@Getter
public class InterestRate extends Transaction {
    private final double amount;
    private final String currency;
    public InterestRate(String description, int timestamp, double amount, String currency) {
        super(timestamp, description);
        this.amount = amount;
        this.currency = currency;
    }
}

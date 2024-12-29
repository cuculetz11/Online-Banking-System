package org.poo.entities.transaction;

import lombok.Getter;

@Getter
public class CardPayment extends Transaction {
    private final double amount;
    private final String commerciant;

    public CardPayment(final double amount, final String commerciant, final int timestamp,
                       final String description) {
        super(timestamp, description);
        this.amount = amount;
        this.commerciant = commerciant;
    }

}

package org.poo.entities.transaction;

import lombok.Getter;

@Getter
public class CashWithdrawal extends Transaction{
    private double amount;
    public CashWithdrawal(final String description, final int timestamp, final double amount) {
        super(timestamp, description);
        this.amount = amount;
    }
}

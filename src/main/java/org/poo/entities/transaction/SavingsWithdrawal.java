package org.poo.entities.transaction;

import lombok.Getter;

@Getter
public class SavingsWithdrawal extends Transaction {
    private final double amount;
    private final String classicAccountIBAN;
    private final String savingsAccountIBAN;
    public SavingsWithdrawal(final String classicAccountIBAN, final String description,
                             final String savingsAccountIBAN, final double amount,
                             final int timestamp) {
        super(timestamp, description);
        this.classicAccountIBAN = classicAccountIBAN;
        this.savingsAccountIBAN = savingsAccountIBAN;
        this.amount = amount;

    }
}

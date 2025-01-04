package org.poo.entities.transaction;

import lombok.Getter;

@Getter
public class NewPlan extends Transaction {
    private final String accountIBAN;
    private final String newPlanType;
    public NewPlan(final String description, final int timestamp, final String accountIBAN,
                   final String newPlanType) {
        super(timestamp, description);
        this.accountIBAN = accountIBAN;
        this.newPlanType = newPlanType;
    }
}

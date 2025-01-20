package org.poo.entities.commerciant;

import lombok.Getter;

@Getter
public class CommerciantBusinessInfo {
    private int timestamp;
    private String userEmail;
    private double amount;
    private String commerciantName;

    public CommerciantBusinessInfo(final int timestamp, final String userEmail,
                                   final double amount, final String commerciantName) {
        this.timestamp = timestamp;
        this.userEmail = userEmail;
        this.amount = amount;
        this.commerciantName = commerciantName;

    }
}

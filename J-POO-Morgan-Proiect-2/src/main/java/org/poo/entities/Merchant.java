package org.poo.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Merchant {
    private final String commerciant;
    @Setter
    private double total;

    public Merchant(final String commerciant, final double amount) {
        this.commerciant = commerciant;
        this.total = amount;
    }

}

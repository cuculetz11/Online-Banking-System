package org.poo.entities.transaction;

import lombok.Getter;

@Getter
public class LifeOfACard extends Transaction {
    private final String card;
    private final String cardHolder;
    private final String account;

    public LifeOfACard(final String description, final String card, final String holder,
                       final int timestamp, final String account) {
        super(timestamp, description);
        this.card = card;
        this.cardHolder = holder;
        this.account = account;
    }

}

package org.poo.entities.card;

public class ClassicCard extends Card {
    public ClassicCard() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(final double amount) {
        super.getAccount().setBalance(super.getAccount().getBalance() - amount);
    }
}

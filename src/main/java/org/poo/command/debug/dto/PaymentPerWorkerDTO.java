package org.poo.command.debug.dto;

import lombok.Getter;

@Getter
public class PaymentPerWorkerDTO {
    private final String username;
    private double spent;
    private double deposited;
    public PaymentPerWorkerDTO(final String name) {
        this.username = name;
        spent = 0;
        deposited = 0;
    }

    /**
     * Adaugam suma data la suma cehltuita
     * @param amount suma data
     */
    public void addSpent(final double amount) {
        spent += amount;
    }

    /**
     * Adugam suma data la suma adugata
     * @param amount suma data
     */
    public void addDeposited(final double amount) {
        deposited += amount;
    }
}

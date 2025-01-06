package org.poo.command.debug.dto;

import lombok.Getter;

@Getter
public class PaymentPerWorkerDTO {
    private String username;
    private double spent;
    private double deposited;
    public PaymentPerWorkerDTO(String name) {
        this.username = name;
        spent = 0;
        deposited = 0;
    }
    public void addSpent(double amount) {
        spent += amount;
    }
    public void addDeposited(double amount) {
        deposited += amount;
    }
}

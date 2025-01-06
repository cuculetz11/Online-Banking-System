package org.poo.entities.users;

import lombok.Getter;

@Getter
public class UserBusinessInfo {
    private int timestamp;
    private double spent;
    private double deposit;

    public UserBusinessInfo(int timestamp, double spent, double deposit) {
        this.timestamp = timestamp;
        this.spent = spent;
        this.deposit = deposit;
    }
    public void addSpentMoney(double amount) {
        this.spent += amount;
    }
    public void addDepositMoney(double amount) {
        this.deposit += amount;
    }

}

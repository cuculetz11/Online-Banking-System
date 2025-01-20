package org.poo.entities.users;

import lombok.Getter;

@Getter
public class UserBusinessInfo {
    private final int timestamp;
    private double spent;
    private double deposit;

    public UserBusinessInfo(final int timestamp, final double spent, final double deposit) {
        this.timestamp = timestamp;
        this.spent = spent;
        this.deposit = deposit;
    }
}

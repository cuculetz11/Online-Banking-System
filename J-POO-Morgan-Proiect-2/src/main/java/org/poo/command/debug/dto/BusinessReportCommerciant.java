package org.poo.command.debug.dto;

import lombok.Getter;
import org.poo.entities.Merchant;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessReportCommerciant {
    private String iban;
    private double balance;
    private String currency;
    private double payLimit;
    private double depositLimit;
    private List<Merchant> commerciants;
    private List<String> mangers;
    private List<String> employees;

    public BusinessReportCommerciant(String iban, double balance, String currency, double payLimit, double depositLimit, List<Merchant> commerciants, List<String> mangers, List<String> employees) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.payLimit = payLimit;
        this.depositLimit = depositLimit;
        this.commerciants = commerciants;
        this.mangers = mangers;
        this.employees = employees;
    }

}

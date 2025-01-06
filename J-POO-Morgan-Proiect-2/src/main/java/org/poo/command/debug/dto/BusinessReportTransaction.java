package org.poo.command.debug.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class BusinessReportTransaction {
    private String iban;
    private double balance;
    private String currency;
    private double payLimit;
    private double depositLimit;
    private String type;
    private List<PaymentPerWorkerDTO> managers;
    private List<PaymentPerWorkerDTO> employees;
    private double total_spent;
    private double total_deposit;

    public BusinessReportTransaction(String iban, String type, double balance, String currency, double payLimit, double depositLimit, List<PaymentPerWorkerDTO> managers, List<PaymentPerWorkerDTO> employees, double total_spent, double total_deposit) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.payLimit = payLimit;
        this.depositLimit = depositLimit;
        this.managers = managers;
        this.employees = employees;
        this.total_spent = total_spent;
        this.total_deposit = total_deposit;
        this.type = type;
    }
}

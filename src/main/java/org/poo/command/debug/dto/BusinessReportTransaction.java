package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
@Getter
public class BusinessReportTransaction {
    @JsonProperty("IBAN")
    private String iban;
    private final double balance;
    private final String currency;
    @JsonProperty("spending limit")
    private double payLimit;
    @JsonProperty("deposit limit")
    private double depositLimit;
    @JsonProperty("statistics type")
    private String type;
    private final List<PaymentPerWorkerDTO> managers;
    private final List<PaymentPerWorkerDTO> employees;
    @JsonProperty("total spent")
    private final double totalSpent;
    @JsonProperty("total deposited")
    private final double totalDeposit;

    public BusinessReportTransaction(final String iban, final String type, final double balance,
                                     final String currency, final double payLimit,
                                     final double depositLimit,
                                     final List<PaymentPerWorkerDTO> managers,
                                     final List<PaymentPerWorkerDTO> employees,
                                     final double totalSpent, final double totalDeposit) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.payLimit = payLimit;
        this.depositLimit = depositLimit;
        this.managers = managers;
        this.employees = employees;
        this.totalSpent = totalSpent;
        this.totalDeposit = totalDeposit;
        this.type = type;
    }
}

package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class BusinessReportCommerciant {
    @JsonProperty("IBAN")
    private String iban;
    private final double balance;
    private final  String currency;
    @JsonProperty("spending limit")
    private double payLimit;
    @JsonProperty("deposit limit")
    private double depositLimit;
    private final List<CommerciantDTO> commerciants;
    @JsonProperty("statistics type")
    private String type;


    public BusinessReportCommerciant(final String iban, final double balance,
                                     final String currency, final double payLimit,
                                     final double depositLimit,
                                     final List<CommerciantDTO> commerciants,
                                     final String type) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.payLimit = payLimit;
        this.depositLimit = depositLimit;
        this.commerciants = commerciants;
        this.type = type;
    }

}

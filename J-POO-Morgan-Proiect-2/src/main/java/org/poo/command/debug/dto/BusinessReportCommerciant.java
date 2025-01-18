package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.poo.entities.Merchant;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessReportCommerciant {
    @JsonProperty("IBAN")
    private String iban;
    private double balance;
    private String currency;
    @JsonProperty("spending limit")
    private double payLimit;
    @JsonProperty("deposit limit")
    private double depositLimit;
    private List<CommerciantDTO> commerciants;
    @JsonProperty("statistics type")
    private String type;


    public BusinessReportCommerciant(String iban, double balance, String currency, double payLimit, double depositLimit, List<CommerciantDTO> commerciants, String type) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.payLimit = payLimit;
        this.depositLimit = depositLimit;
        this.commerciants = commerciants;
        this.type = type;
    }

}

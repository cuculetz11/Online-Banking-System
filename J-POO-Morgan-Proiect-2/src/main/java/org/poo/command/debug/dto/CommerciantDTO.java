package org.poo.command.debug.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommerciantDTO {
    private String commerciant;
    private double totalReceived;
    private List<String> mangers;
    private List<String> employers;
    public CommerciantDTO(String commerciant, double totalReceived, List<String> mangers, List<String> employers) {
        this.commerciant = commerciant;
        this.totalReceived = totalReceived;
        this.mangers = mangers;
        this.employers = employers;
    }
}

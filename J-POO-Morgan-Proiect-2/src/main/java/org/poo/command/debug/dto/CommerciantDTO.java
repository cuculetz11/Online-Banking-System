package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CommerciantDTO {
    private String commerciant;
    @JsonProperty("total received")
    private double totalReceived;
    private List<String> managers;
    private List<String> employees;
    public CommerciantDTO(String commerciant, double totalReceived, List<String> mangers, List<String> employers) {
        this.commerciant = commerciant;
        this.totalReceived = totalReceived;
        this.managers = mangers;
        this.employees = employers;
    }
    public void addMoney(double money) {
        totalReceived += money;
    }
    public void addManger(String manger) {
        managers.add(manger);
    }
    public void addEmployer(String employer) {
        employees.add(employer);
    }

}

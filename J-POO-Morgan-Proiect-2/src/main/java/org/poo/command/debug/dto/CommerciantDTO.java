package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CommerciantDTO {
    private final String commerciant;
    @JsonProperty("total received")
    private double totalReceived;
    private final List<String> managers;
    private final List<String> employees;
    public CommerciantDTO(final String commerciant, final double totalReceived,
                          final List<String> mangers, final List<String> employers) {
        this.commerciant = commerciant;
        this.totalReceived = totalReceived;
        this.managers = mangers;
        this.employees = employers;
    }

    /**
     * Adugam bani la suma ce era inainte
     * @param money suma de inainte
     */
    public void addMoney(final double money) {
        totalReceived += money;
    }

    /**
     * Adugam managerii ce au facut o plata catre comerciantul respectiv
     * @param manger numele managerului pe care-l adugam
     */
    public void addManger(final String manger) {
        managers.add(manger);
    }

    /**
     * Adugam angajatii ce au facut o plata catre comerciantul respectiv
     * @param employer numele angajatului pe care-l adaugam
     */
    public void addEmployer(final String employer) {
        employees.add(employer);
    }

}

package org.poo.entities.commerciant;

import lombok.Getter;
import org.poo.fileio.CommerciantInput;

@Getter
public class Commerciant {
    private int id;
    private String commerciant;
    private String account;
    private String cashbackStrategy;
    private String type;
    public Commerciant(final CommerciantInput commerciantInput) {
        this.id = commerciantInput.getId();
        this.commerciant = commerciantInput.getCommerciant();
        this.account = commerciantInput.getAccount();
        this.cashbackStrategy = commerciantInput.getCashbackStrategy();
        this.type = commerciantInput.getType();
    }
}

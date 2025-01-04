package org.poo.services.initialize;

import org.poo.entities.Bank;
import org.poo.entities.commerciant.Commerciant;
import org.poo.fileio.CommerciantInput;
import org.poo.services.BankMethods;

public class CommerciantInitialize implements BankMethods {
    private CommerciantInput[] commerciantInputs;

    public CommerciantInitialize(CommerciantInput[] commerciantInputs) {
        this.commerciantInputs = commerciantInputs;
    }

    @Override
    public void visit(Bank bank) {
        for (CommerciantInput commerciantInput : commerciantInputs) {
            Commerciant commerciant = new Commerciant(commerciantInput);
            bank.getCommerciants().put(commerciant.getCommerciant(), commerciant);
            bank.getCommerciants().put(commerciant.getAccount(), commerciant);
        }
    }
}

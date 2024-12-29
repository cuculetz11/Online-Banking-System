package org.poo.command;

import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;

public class SetMinBalance implements Command {
    /**
     * Seteaza un o valoare minima pentru suma din cont
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Bank.getInstance().getAccounts().get(input.getAccount())
                .setMinimumBalance(input.getAmount());
    }
}

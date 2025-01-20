package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;

public class AddInterest implements Command {
    /**
     * Adauga dobanda obtinuta la contul de economii
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        account.addInterest(input);
    }
}

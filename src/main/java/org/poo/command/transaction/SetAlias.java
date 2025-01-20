package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;

public class SetAlias implements Command {
    /**
     * Se pune un alias unui cont
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Account account = Bank.getInstance().getUsers().get(input.getEmail()).getAccounts()
                .get(input.getAccount());
        Bank.getInstance().getUsers().get(input.getEmail()).getAccounts()
                .put(input.getAlias(), account);
        Bank.getInstance().getAccounts().put(input.getAlias(), account);
    }
}

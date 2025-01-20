package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.command.debug.dto.AccountDeleteInfo;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;

public class DeleteAccount implements Command {
    /**
     * Sterge contul bancar daca e gol
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    public void execute(final CommandInput input) {
        AccountDeleteInfo data = null;
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        account.deleteAccount(input);
    }
}

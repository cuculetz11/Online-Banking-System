package org.poo.command.report;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.ErrorManager;

public class SpendingReport implements Command {
    /**
     * Raport de cheltuieli
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if (account == null) {
            ErrorManager.notFound(Constants.ACCOUNT_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            return;
        }
        account.spendingReport(input);
    }
}

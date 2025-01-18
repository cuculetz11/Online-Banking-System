package org.poo.command.report;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.ErrorManager;
import org.poo.utils.observer.CheckAccountsPrecision;

public class BusinessReport implements Command {
    @Override
    public void execute(CommandInput input) {
//        CheckAccountsPrecision checkAccountsPrecision = new CheckAccountsPrecision();
//        BANKING_SERVICES.acceptVisitor(checkAccountsPrecision);
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if (account == null) {
            ErrorManager.notFound(Constants.ACCOUNT_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            return;
        }
        account.spendingReport(input);
    }
}

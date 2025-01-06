package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.command.debug.dto.AccountDeleteInfo;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.command.debug.dto.DeleteAccountDTO;
import org.poo.command.debug.dto.ErrorPrint;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.JsonOutManager;
import org.poo.utils.TransactionManager;
import org.poo.utils.observer.CheckAccountsPrecision;

public class DeleteAccount implements Command {
    /**
     * Sterge contul bancar daca e gol
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    public void execute(final CommandInput input) {
        CheckAccountsPrecision checkAccountsPrecision = new CheckAccountsPrecision();
        BANKING_SERVICES.acceptVisitor(checkAccountsPrecision);
        AccountDeleteInfo data = null;
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        account.deleteAccount(input);
    }
}

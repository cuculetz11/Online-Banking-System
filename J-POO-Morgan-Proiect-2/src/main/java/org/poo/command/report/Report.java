package org.poo.command.report;

import org.poo.command.Command;
import org.poo.command.debug.dto.AccountHistoryDTO;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.ErrorManager;
import org.poo.utils.JsonOutManager;

import java.util.ArrayList;

public class Report implements Command {
    /**
     * Face un raport pentru contul dat
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());

        if (account == null) {
            ErrorManager.notFound(Constants.ACCOUNT_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            return;
        }
        ArrayList<Transaction> accountTransactions =
                Bank.getInstance().getAccounts().get(input.getAccount()).getTransactionsHistory();
        if (accountTransactions == null) {
            return;
        }
        for (Transaction transaction : accountTransactions) {
            if (transaction.getTimestamp() > input.getEndTimestamp()) {
                break;
            }
            if (transaction.getTimestamp() >= input.getStartTimestamp()) {
                transactions.add(transaction);
            }
        }
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO(input.getAccount(),
                account.getBalance(), account.getCurrency(), transactions);

        DebugActionsDTO<AccountHistoryDTO> report = new DebugActionsDTO<>(input.getCommand(),
                accountHistoryDTO, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(report);

    }
}

package org.poo.command.business;

import org.poo.command.Command;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BusinessAccount;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.utils.ErrorManager;
import org.poo.utils.JsonOutManager;

public class ChangeDepositLimit implements Command {
    @Override
    public void execute(CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if(account == null) {
            System.out.println("Account not found");
            return;
        }
        if(!account.getType().equals("business")) {
            ErrorManager.notFound("This is not a business account",input.getCommand(), input.getTimestamp());
            return;
        }
        BusinessAccount businessAccount = (BusinessAccount) account;
        if(!businessAccount.getUser().getEmail().equals(input.getEmail())) {
            Transaction err = new Transaction(input.getTimestamp(), "You must be owner in order to change deposit limit.");
            DebugActionsDTO<Transaction> debugActionsDTO = new DebugActionsDTO<>(input.getCommand(), err, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);

            return;
        }
        businessAccount.setDepositLimit(input.getAmount());
    }
}

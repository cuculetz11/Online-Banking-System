package org.poo.command.business;

import org.poo.command.Command;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BussinessAccount;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutManager;

public class ChangeSpendingLimit implements Command {
    @Override
    public void execute(CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if(account == null) {
            System.out.println("Account not found");
            return;
        }
        if(!account.getType().equals("business")) {
            System.out.println("Account type is not business");
            return;
        }
        BussinessAccount businessAccount = (BussinessAccount) account;
        if(!businessAccount.getUser().getEmail().equals(input.getEmail())) {
            Transaction err = new Transaction(input.getTimestamp(), "You must be owner in order to change spending limit.");
            DebugActionsDTO<Transaction> debugActionsDTO = new DebugActionsDTO<>(input.getCommand(), err, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(debugActionsDTO);

            return;
        }
        businessAccount.setPayLimit(input.getAmount());
    }
}

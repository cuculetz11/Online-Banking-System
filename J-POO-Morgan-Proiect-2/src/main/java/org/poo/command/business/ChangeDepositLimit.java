package org.poo.command.business;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BussinessAccount;
import org.poo.fileio.CommandInput;

public class ChangeDepositLimit implements Command {
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
            System.out.println("User email nu e sefu");
            return;
        }
        businessAccount.setDepositLimit(input.getAmount());
    }
}

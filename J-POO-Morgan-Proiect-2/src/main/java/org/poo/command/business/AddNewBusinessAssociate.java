package org.poo.command.business;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BusinessAccount;
import org.poo.fileio.CommandInput;

public class AddNewBusinessAssociate implements Command {
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
        BusinessAccount businessAccount = (BusinessAccount) account;
        if(businessAccount.getUser().getEmail().equals(input.getEmail()) || businessAccount.getManagers().contains(input.getEmail()) || businessAccount.getEmployees().contains(input.getEmail())) {
            System.out.println("Account already exists");
            return;
        }
        if(input.getRole().equals("manager")) {
            businessAccount.getManagers().add(input.getEmail());
        } else if(input.getRole().equals("employee")) {
            businessAccount.getEmployees().add(input.getEmail());
        }
    }
}

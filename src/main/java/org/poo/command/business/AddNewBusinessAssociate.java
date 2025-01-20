package org.poo.command.business;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BusinessAccount;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;

public class AddNewBusinessAssociate implements Command {
    /**
     * Adaugam un nou asociat in contul de business dat
     * Se verifica daca acesta este deja in firma
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if (account == null) {
            return;
        }
        if (!account.getType().equals(Constants.BUSINESS)) {
            return;
        }
        BusinessAccount businessAccount = (BusinessAccount) account;
        if (businessAccount.getUser().getEmail().equals(input.getEmail())
                || businessAccount.getManagers().contains(input.getEmail())
                || businessAccount.getEmployees().contains(input.getEmail())) {
            return;
        }
        if (input.getRole().equals(Constants.MANAGER)) {
            businessAccount.getManagers().add(input.getEmail());
        } else if (input.getRole().equals(Constants.EMPLOYEE)) {
            businessAccount.getEmployees().add(input.getEmail());
        }
    }
}

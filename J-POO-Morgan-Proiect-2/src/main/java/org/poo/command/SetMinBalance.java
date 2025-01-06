package org.poo.command;

import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;

public class SetMinBalance implements Command {
    /**
     * Seteaza un o valoare minima pentru suma din cont
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if(account == null){
            System.out.println("Account not found");
            return;
        }
       account.setMinBalance(input);
    }
}

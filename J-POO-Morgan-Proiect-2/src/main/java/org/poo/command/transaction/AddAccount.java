package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;
import org.poo.utils.factory.AccountFactory;

public class AddAccount implements Command {
    /**
     * Adauga cun cont
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        try {
            Account account = AccountFactory.getAccount(input);
            if (account == null) {
              throw new IllegalArgumentException("Utilizatorul nu exista" + input.getEmail());
            }
            String userEmail = input.getEmail();
            BANKING_SERVICES.addAccount(account, account.getIban(), userEmail);

            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.NEW_ACCOUNT_CREATED,
                            input.getTimestamp())
                            .transactionName(Constants.ADD_ACCOUNT_TRANSACTION)
                            .iban(account.getIban())
                            .userEmail(userEmail)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
        } catch (IllegalArgumentException e) {
            System.err.println("Erroare: " + e.getMessage());
        }
    }
}

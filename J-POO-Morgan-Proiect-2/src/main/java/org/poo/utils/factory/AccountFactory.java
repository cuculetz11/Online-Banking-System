package org.poo.utils.factory;

import org.poo.entities.bankAccount.Account;
import org.poo.entities.bankAccount.BusinessAccount;
import org.poo.entities.bankAccount.ClassicAccount;
import org.poo.entities.bankAccount.SavingsAccount;
import org.poo.fileio.CommandInput;

public final class AccountFactory {
    private AccountFactory() { }

    /**
     * Un mic factory pentru fiecare cont (poate pe viitor o sa creasca)
     * @param input comanda ce contine si tipul contului
     * @return returneaza un cont cerut
     */
    public static Account getAccount(final CommandInput input) {
        return switch (input.getAccountType()) {
            case "classic" -> new ClassicAccount(input);
            case "savings" -> new SavingsAccount(input);
            case "business" -> new BusinessAccount(input);
            default -> null;
        };
    }
}
